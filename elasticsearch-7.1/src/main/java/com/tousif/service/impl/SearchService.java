package com.tousif.service.impl;



import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tousif.client.SearchClient;


@Component
public class SearchService {

	@Autowired
	private SearchClient searchClient;
	
	/**
	 * @SEARCH_REQUEST
	 * The SearchRequest is used for any operation that has to do with searching documents, aggregations, suggestions
	 * and also offers ways of requesting highlighting on the resulting documents.
	 * In its most basic form, we can add a query to the request:
	 * 
	 * 1. Creates the SeachRequest. Without arguments this runs against all indices.
	 * 2. Most search parameters are added to the SearchSourceBuilder. It offers setters for everything that goes into the search request body.
	 * 3. Add a match_all query to the SearchSourceBuilder.
	 * 4. Add the SearchSourceBuilder to the SeachRequest.
	 */
	public SearchResponse searchAll() {

		RestHighLevelClient client = searchClient.getClient();
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 

		
		SearchRequest searchRequest = new SearchRequest(); //Search all indices
		
		/**
		 * @OPTIONAL_ARGUMENTS
		 * Restricts the request to an index
		 */
//		SearchRequest searchRequest = new SearchRequest("vca_mdm_index"); //search only passed index

		searchRequest.source(searchSourceBuilder);
		
		RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
		
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, COMMON_OPTIONS);
			System.out.println("\n\n"+searchResponse+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
		
//		CfgBusinessIndustry cfgBusinessIndustry = result.getFirstHit(CfgBusinessIndustry.class).source;
		
	}

	
	/**
	 * @SEARCH_SOURCE_BUILDER
	 * Most options controlling the search behavior can be set on the SearchSourceBuilder, which contains more or less the equivalent
	 * of the options in the search request body of the Rest API.
	 * 
	 * 1. Create a SearchSourceBuilder with default options.
	 * 2. Set the query. Can be any type of QueryBuilder
	 * 3. Set the from option that determines the result index to start searching from. Defaults to 0.
	 * 4. Set the size option that determines the number of search hits to return. Defaults to 10.
	 * 5. Set an optional timeout that controls how long the search is allowed to take.
	 * 6. After this, the SearchSourceBuilder only needs to be added to the SearchRequest:
	 */
	public SearchResponse searchAllWithinIndex(String index) {
		
		RestHighLevelClient client = searchClient.getClient();
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 

		/**
		 * @OPTIONAL_ARGUMENTS
		 * Restricts the request to an index
		 */
//		SearchRequest searchRequest = new SearchRequest("vca_mdm_index"); //search only passed index
		
		SearchRequest searchRequest = new SearchRequest(); 
		searchRequest.indices(index).source(searchSourceBuilder);
		
		RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
		
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, COMMON_OPTIONS);
			System.out.println("\n\n"+searchResponse+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
	}

	
	/**
	 * @BUILDING_QUERIES
	 * Search queries are created using QueryBuilder objects. A QueryBuilder exists for every search query type supported by Elasticsearch’s Query DSL.
	 * 
	 * @QUERY_BUILDER
	 * A QueryBuilder can be created using its constructor:
	 * 
	 * MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("user", "kimchy");
	 * 
	 * Create a full text Match Query that matches the text "kimchy" over the field "user".
	 */
	public SearchResponse searchFieldInIndex(String index, String fieldName, String fieldValue) {
		RestHighLevelClient client = searchClient.getClient();
		
		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(fieldName, fieldValue);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
//		searchSourceBuilder.query(QueryBuilders.existsQuery(field)); 
		searchSourceBuilder.query(matchQueryBuilder);
		
		SearchRequest searchRequest = new SearchRequest(); 
		searchRequest.source(searchSourceBuilder).indices(index);
		
		RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
		
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, COMMON_OPTIONS);
			System.out.println("\n\n"+searchResponse+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
	}
	
	
	/**
	 * @FUZZY_MATCHING
	 * Once created, the QueryBuilder object provides methods to configure the options of the search query it creates:
	 * 
	 * matchQueryBuilder.fuzziness(Fuzziness.AUTO); 
	 * matchQueryBuilder.prefixLength(3); 
	 * matchQueryBuilder.maxExpansions(10);
	 * 
	 * 1. Enable fuzzy matching on the match query
	 * 2. Set the prefix length option on the match query
	 * 3. Set the max expansion options to control the fuzzy process of the query
	 */
	public SearchResponse searchFieldInIndexFuzziness(String index, String fieldName, String fieldValue) {
		RestHighLevelClient client = searchClient.getClient();
		
		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(fieldName, fieldValue);
		matchQueryBuilder.fuzziness(Fuzziness.AUTO); 
		matchQueryBuilder.prefixLength(3); 
		matchQueryBuilder.maxExpansions(10);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
//		searchSourceBuilder.query(QueryBuilders.existsQuery(field)); 
		searchSourceBuilder.query(matchQueryBuilder);
		
		
		SearchRequest searchRequest = new SearchRequest(); 
		searchRequest.source(searchSourceBuilder).indices(index);
		
		RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
		
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, COMMON_OPTIONS);
			System.out.println("\n\n"+searchResponse+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
	}
	
	
	
	
	/**
	 * @QUERY_BUILDER
	 * QueryBuilder objects can also be created using the QueryBuilders utility class. This class provides
	 * helper methods that can be used to create QueryBuilder objects using a fluent programming style.
	 */
	public SearchResponse searchOptions(String index, String fieldName, String fieldValue) {
		RestHighLevelClient client = searchClient.getClient();
		
//		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(fieldName, fieldValue);
//		matchQueryBuilder.fuzziness(Fuzziness.AUTO); 
//		matchQueryBuilder.prefixLength(3); 
//		matchQueryBuilder.maxExpansions(10);

		QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldName, fieldValue)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10);
		
		/**
		 * Whatever the method used to create it, the QueryBuilder object must be added to the SearchSourceBuilder as follows:
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
//		searchSourceBuilder.query(QueryBuilders.existsQuery(field)); 
		searchSourceBuilder.query(matchQueryBuilder);
		
		SearchRequest searchRequest = new SearchRequest(); 
		searchRequest.source(searchSourceBuilder).indices(index);
		

		/**
		 * @SOURCE_FILTERING
		 * By default, search requests return the contents of the document _source but like in the Rest API you can overwrite this behavior.
		 * For example, you can turn off _source retrieval completely:
		 */
//		searchSourceBuilder.fetchSource(false);
		
		/**
		 * The method also accepts an array of one or more wildcard patterns to control which fields get included or excluded in a more fine grained way:
		 */
		String[] includeFields = new String[] {"country", "state", "cfg_*"};
//		String[] includeFields = null;
//		String[] excludeFields = new String[] {"section", "established_date"};
		String[] excludeFields = null;
		searchSourceBuilder.fetchSource(includeFields, excludeFields);
		
		/**
		 * @SPECIFYING_SORTING
		 * The SearchSourceBuilder allows to add one or more SortBuilder instances. There are four special implementations
		 * (Field-, Score-, GeoDistance- and ScriptSortBuilder).
		 */
		searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));
		
		RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
		
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, COMMON_OPTIONS);
			System.out.println("\n\n"+searchResponse+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
	}
	
	/**
	 * @REQUESTING_HIGHLIGHTING
	 * Highlighting search results can be achieved by setting a HighlightBuilder on the SearchSourceBuilder.
	 * Different highlighting behaviour can be defined for each fields by adding one or more HighlightBuilder.
	 * Field instances to a HighlightBuilder.
	 * 
	 * 1. Creates a new HighlightBuilder
	 * 2. Create a field highlighter for the title field
	 * 3. Set the field highlighter type
	 * 4. Add the field highlighter to the highlight builder
	 */
	public SearchResponse requestingHighlighting(String index, String fieldName, String fieldValue) {
		RestHighLevelClient client = searchClient.getClient();
		
		QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldName, fieldValue)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10);
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(matchQueryBuilder);
		
		SearchRequest searchRequest = new SearchRequest(); 
		searchRequest.source(searchSourceBuilder).indices(index);
		
//		searchSourceBuilder.fetchSource(false);
		searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));
		
		HighlightBuilder highlightBuilder = new HighlightBuilder(); 
		
		HighlightBuilder.Field highlightState = new HighlightBuilder.Field("state");
		highlightState.highlighterType("unified");
		highlightBuilder.field(highlightState);  
		
		HighlightBuilder.Field highlightCountry = new HighlightBuilder.Field("country");
		highlightBuilder.field(highlightCountry);
		
		searchSourceBuilder.highlighter(highlightBuilder);
		
		RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
		
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, COMMON_OPTIONS);
			System.out.println("\n\n"+searchResponse+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
	}
	
	/**
	 * @REQUESTING_AGGREGATIONS
	 * Aggregations can be added to the search by first creating the appropriate AggregationBuilder and then setting it on the SearchSourceBuilder.
	 * In the following example we create a terms aggregation on "lowest_generic_line_hierarchy_id" with a sub-aggregation on the average number:
	 */
	public SearchResponse requestingAggregations(String index, String fieldName, String fieldValue) {
		
		RestHighLevelClient client = searchClient.getClient();

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		
		SearchRequest searchRequest = new SearchRequest(index);
		
		RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
		
//		TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
//		        .field("lowest_generic_line_hierarchy_id");
//		
//		aggregation.subAggregation(AggregationBuilders.avg("lowest_generic_line_hierarchy_id")
//		        .field("lowest_generic_line_hierarchy_id"));
		
		AggregationBuilder aggregation = AggregationBuilders
				.global("aggs")
				.subAggregation(
						AggregationBuilders
						.terms("lowest_generic_line_hierarchy_id")
						.field("lowest_generic_line_hierarchy_id"));
		
		
		searchSourceBuilder.aggregation(aggregation);
		
		
		
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, COMMON_OPTIONS);
			System.out.println("\n\n"+searchResponse+"\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResponse;
	}
}
