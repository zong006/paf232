package vttp2023.batch4.paf.assessment.repositories;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	private static final String mongoConstant_cName_listing = "listings";
	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	
		 db.getCollection("listings").aggregate([
			{
				$group:{
					"_id":"$address.suburb"
					
				}
			},
			{
				$sort:{
					"_id":1
				}
			},
			{
				$match:{
					"_id":{$ne:""}
				}   
			}
		])


	 */
	public List<String> getSuburbs(String country) {

		GroupOperation groupBySuburb = Aggregation.group("address.suburb");
		SortOperation sortByAlphabetical = Aggregation.sort(Sort.by(Direction.ASC, "_id"));
		MatchOperation removeEmptyString = Aggregation.match(Criteria.where("_id").ne(""));

		Aggregation pipeline = Aggregation.newAggregation(groupBySuburb, sortByAlphabetical, removeEmptyString);
		List<Document> results = template.aggregate(pipeline, mongoConstant_cName_listing,Document.class).getMappedResults();
		List<String> suburbs = results.stream().map(x -> (String) x.get("_id")).toList();

		return suburbs;
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *
	 * 	db.getCollection("listings").find(
			{
				$and:[
					{
						"address.suburb":{
							$regex: "Annandale", $options: "i"
						}
					},
					{
						"price":{$lte:100}
					},
					{
						"accommodates":{$gte:1}
					},
					{
						"min_nights":{$lte:4}
					}
				]
			},
			{
				"_id":1, "name":1, "accommodates":1, "price":1
			}
		)
	 * 
	 * 
	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		Criteria suburbCriteria = Criteria.where("address.suburb").regex(suburb, "i");
		Criteria priceCriteria = Criteria.where("price").lte(priceRange);
		Criteria minNightCriteria = Criteria.where("min_nights").lte(duration);
		
		List<Criteria> criterias = Arrays.asList(suburbCriteria, priceCriteria, minNightCriteria);
		Criteria criteria = new Criteria().andOperator(criterias);
		Query query = Query.query(criteria);
		List<Document> results = template.find(query, Document.class, mongoConstant_cName_listing);

		List<AccommodationSummary> summaries = results.stream()
														.map((x) -> {
														
															AccommodationSummary as = new AccommodationSummary();
															as.setId((String)x.get("_id"));
															as.setName(x.getString("name"));
															Decimal128 price = (Decimal128) x.get("price");
															as.setPrice(price.floatValue());
															as.setAccomodates(x.getInteger("accommodates"));

															return as;
														}).toList();


		return summaries;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
