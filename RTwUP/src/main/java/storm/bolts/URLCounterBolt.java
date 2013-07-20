package storm.bolts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import storage.URLMap;
import view.PageDictionary;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * This bolt counts the URL occurrences.
 * 
 * @author Gabriele de Capoa, Gabriele Proni, Daniele Morgantini
 * 
 */

public class URLCounterBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(URLCounterBolt.class);
	
	public void prepare(Map conf, TopologyContext context) {
	}

	public void execute(Tuple input, BasicOutputCollector collector) {

		String domain = input.getStringByField("expanded_url_domain");
		String path = input.getStringByField("expanded_url_complete"); 
		Integer count = PageDictionary.getInstance(7).addToDictionary(domain, path);
		
		String message = "Domain: " + domain + " URL: " + path + " Count: "
				+ count;
		LOGGER.info(message);
		
		collector.emit(new Values(message));
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message"));
	}

	
}