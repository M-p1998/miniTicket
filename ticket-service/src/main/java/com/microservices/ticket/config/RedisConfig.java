//package com.microservices.ticket.config;
//
//import java.time.Duration;
//
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
//@Configuration
//@EnableCaching
//public class RedisConfig {
//	
////	@Bean
////    public RedisCacheConfiguration cacheConfiguration() {
////
////        ObjectMapper mapper = new ObjectMapper();
////        mapper.registerModule(new JavaTimeModule());
////        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
////
////        GenericJackson2JsonRedisSerializer serializer =
////                new GenericJackson2JsonRedisSerializer(mapper);
////
////        return RedisCacheConfiguration.defaultCacheConfig()
////                .serializeValuesWith(
////                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
////                );
////    }
//	
//	
//	@Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        GenericJackson2JsonRedisSerializer serializer =
//                new GenericJackson2JsonRedisSerializer(mapper);
//
//        RedisCacheConfiguration config =
//                RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(5))
//                        .serializeValuesWith(
//                                RedisSerializationContext.SerializationPair
//                                        .fromSerializer(serializer)
//                        );
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
//
//}




package com.microservices.ticket.config;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservices.ticket.dto.TicketResponse;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;



@Configuration
@EnableCaching
public class RedisConfig {

//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.registerModule(new JavaTimeModule());
//
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        mapper.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//
//        GenericJackson2JsonRedisSerializer serializer =
//                new GenericJackson2JsonRedisSerializer(mapper);
//
//        RedisCacheConfiguration config =
//                RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(5))
//                        .serializeValuesWith(
//                                RedisSerializationContext.SerializationPair
//                                        .fromSerializer(serializer)
//                        );
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
	
	
//	@Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        mapper.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//
//        GenericJackson2JsonRedisSerializer serializer =
//                new GenericJackson2JsonRedisSerializer(mapper);
//
//        RedisCacheConfiguration config =
//                RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(5))
//                        .serializeValuesWith(
//                                RedisSerializationContext.SerializationPair
//                                        .fromSerializer(serializer)
//                        );
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
	
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(new JavaTimeModule());
//	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//	    mapper.activateDefaultTyping(
//	            LaissezFaireSubTypeValidator.instance,
//	            ObjectMapper.DefaultTyping.NON_FINAL,
//	            JsonTypeInfo.As.PROPERTY
//	    );
//
//	    GenericJackson2JsonRedisSerializer serializer =
//	            new GenericJackson2JsonRedisSerializer(mapper);
//
//	    RedisCacheConfiguration config =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeValuesWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(serializer)
//	                    );
//
//	    return RedisCacheManager.builder(connectionFactory)
//	            .cacheDefaults(config)
//	            .build();
//	}
	
//	@Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//
//        // 🔥 Enable type info so Redis can deserialize ANY object
//        mapper.activateDefaultTyping(
//                mapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.NON_FINAL
//        );
//
//        GenericJackson2JsonRedisSerializer serializer =
//                new GenericJackson2JsonRedisSerializer(mapper);
//
//        RedisCacheConfiguration config =
//                RedisCacheConfiguration.defaultCacheConfig()
//                        .serializeKeysWith(
//                                RedisSerializationContext.SerializationPair
//                                        .fromSerializer(new StringRedisSerializer())
//                        )
//                        .serializeValuesWith(
//                                RedisSerializationContext.SerializationPair
//                                        .fromSerializer(serializer)
//                        );
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
	
	
//	 @Bean
//	    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	        ObjectMapper mapper = new ObjectMapper();
//	        mapper.registerModule(new JavaTimeModule());
//	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//	        mapper.activateDefaultTyping(
//	                mapper.getPolymorphicTypeValidator(),
//	                ObjectMapper.DefaultTyping.NON_FINAL,
//	                JsonTypeInfo.As.PROPERTY   // Force stable format
//	        );
//
//	        GenericJackson2JsonRedisSerializer serializer =
//	                new GenericJackson2JsonRedisSerializer(mapper);
//
//	        RedisCacheConfiguration config =
//	                RedisCacheConfiguration.defaultCacheConfig()
//	                        .serializeKeysWith(
//	                                RedisSerializationContext.SerializationPair
//	                                        .fromSerializer(new StringRedisSerializer())
//	                        )
//	                        .serializeValuesWith(
//	                                RedisSerializationContext.SerializationPair
//	                                        .fromSerializer(serializer)
//	                        );
//
//	        return RedisCacheManager.builder(connectionFactory)
//	                .cacheDefaults(config)
//	                .build();
//	    }
	
	
	
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(new JavaTimeModule());
//	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//	    GenericJackson2JsonRedisSerializer serializer =
//	            new GenericJackson2JsonRedisSerializer(mapper);
//
//	    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//	            .serializeKeysWith(
//	                    RedisSerializationContext.SerializationPair.fromSerializer(
//	                            new StringRedisSerializer()
//	                    )
//	            )
//	            .serializeValuesWith(
//	                    RedisSerializationContext.SerializationPair.fromSerializer(
//	                            serializer
//	                    )
//	            );
//
//	    return RedisCacheManager.builder(connectionFactory)
//	            .cacheDefaults(config)
//	            .build();
//	}
	
	
	@Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Serializer that KNOWS it's always TicketResponse — no @class needed
        Jackson2JsonRedisSerializer<TicketResponse> ticketSerializer =
            new Jackson2JsonRedisSerializer<>(mapper, TicketResponse.class);

        // Serializer that KNOWS it's always List<TicketResponse>
        JavaType listOfTickets = mapper.getTypeFactory()
            .constructCollectionType(List.class, TicketResponse.class);
        Jackson2JsonRedisSerializer<List<TicketResponse>> listSerializer =
            new Jackson2JsonRedisSerializer<>(mapper, listOfTickets);

        RedisCacheConfiguration ticketByIdConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(ticketSerializer)
            );

        RedisCacheConfiguration ticketsConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(listSerializer)
            );

        return RedisCacheManager.builder(connectionFactory)
            .withCacheConfiguration("ticketById", ticketByIdConfig)
            .withCacheConfiguration("tickets", ticketsConfig)
            .build();
    }
	
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(new JavaTimeModule());
//	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//	    
//	    // Serializer for TicketResponse
//	    Jackson2JsonRedisSerializer<TicketResponse> ticketSerializer =
//	            new Jackson2JsonRedisSerializer<>(TicketResponse.class);
//	    ticketSerializer.setObjectMapper(mapper);
//
//	    // Serializer for List<TicketResponse>
//	    Jackson2JsonRedisSerializer<List> listSerializer =
//	            new Jackson2JsonRedisSerializer<>(List.class);
//	    listSerializer.setObjectMapper(mapper);
//
//	    RedisCacheConfiguration ticketByIdConfig =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//	                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(ticketSerializer));
//
//	    RedisCacheConfiguration ticketsConfig =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//	                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(listSerializer));
//
//	    Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
//	    cacheConfigs.put("ticketById", ticketByIdConfig);
//	    cacheConfigs.put("tickets", ticketsConfig);
//
//	    return RedisCacheManager.builder(connectionFactory)
//	            .withInitialCacheConfigurations(cacheConfigs)
//	            .build();
//	}
	
	
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(new JavaTimeModule());
//	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//	    mapper.activateDefaultTyping(
//	            mapper.getPolymorphicTypeValidator(),
//	            ObjectMapper.DefaultTyping.NON_FINAL
//	    );
//
////	    GenericJackson2JsonRedisSerializer serializer =
////	            new GenericJackson2JsonRedisSerializer(mapper);
////
////	    RedisCacheConfiguration config =
////	            RedisCacheConfiguration.defaultCacheConfig()
////	                    .serializeKeysWith(
////	                            RedisSerializationContext.SerializationPair.fromSerializer(
////	                                    new StringRedisSerializer()
////	                            )
////	                    )
////	                    .serializeValuesWith(
////	                            RedisSerializationContext.SerializationPair.fromSerializer(
////	                                    serializer
////	                            )
////	                    );
////
////	    return RedisCacheManager.builder(connectionFactory)
////	            .cacheDefaults(config)
////	            .build();
//	    
//	    
////	    // Serializer for TicketResponse
//	    Jackson2JsonRedisSerializer<TicketResponse> ticketSerializer =
//	            new Jackson2JsonRedisSerializer<>(TicketResponse.class);
//	    ticketSerializer.setObjectMapper(mapper);
//
//	    // Serializer for List<TicketResponse>
////	    Jackson2JsonRedisSerializer<List> listSerializer =
////	            new Jackson2JsonRedisSerializer<>(List.class);
////	    listSerializer.setObjectMapper(mapper);
//
//	    RedisCacheConfiguration ticketByIdConfig =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//	                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(ticketSerializer));
//
////	    RedisCacheConfiguration ticketsConfig =
////	            RedisCacheConfiguration.defaultCacheConfig()
////	                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
////	                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(listSerializer));
//
//	    Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
//	    cacheConfigs.put("ticketById", ticketByIdConfig);
////	    cacheConfigs.put("tickets", ticketsConfig);
//
//	    return RedisCacheManager.builder(connectionFactory)
//	            .withInitialCacheConfigurations(cacheConfigs)
//	            .build();
//	}

	
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(new JavaTimeModule());
//	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//	    // tells Jackson to embed type info
//	    mapper.activateDefaultTyping(
//	        BasicPolymorphicTypeValidator.builder()
//	            .allowIfBaseType(Object.class)
//	            .build(),
//	        ObjectMapper.DefaultTyping.NON_FINAL,
//	        JsonTypeInfo.As.PROPERTY
//	    );
//
//	    GenericJackson2JsonRedisSerializer serializer =
//	            new GenericJackson2JsonRedisSerializer(mapper);
//
//	    RedisCacheConfiguration config =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(
//	                            RedisSerializationContext.SerializationPair.fromSerializer(
//	                                    new StringRedisSerializer()
//	                            )
//	                    )
//	                    .serializeValuesWith(
//	                            RedisSerializationContext.SerializationPair.fromSerializer(
//	                                    serializer
//	                            )
//	                    );
//
//	    return RedisCacheManager.builder(connectionFactory)
//	            .cacheDefaults(config)
//	            .build();
//	}
	
	


	
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(new JavaTimeModule());
//
//	    Jackson2JsonRedisSerializer<TicketResponse> ticketSerializer =
//	            new Jackson2JsonRedisSerializer<>(TicketResponse.class);
//	    ticketSerializer.setObjectMapper(mapper);
//
//	    Jackson2JsonRedisSerializer<List> listSerializer =
//	            new Jackson2JsonRedisSerializer<>(List.class);
//	    listSerializer.setObjectMapper(mapper);
//
//	    RedisCacheConfiguration ticketByIdConfig =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(new StringRedisSerializer())
//	                    )
//	                    .serializeValuesWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(ticketSerializer)
//	                    );
//
//	    RedisCacheConfiguration ticketsConfig =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(new StringRedisSerializer())
//	                    )
//	                    .serializeValuesWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(listSerializer)
//	                    );
//
//	    return RedisCacheManager.builder(connectionFactory)
//	            .withCacheConfiguration("ticketById", ticketByIdConfig)
//	            .withCacheConfiguration("tickets", ticketsConfig)
//	            .build();
//	}

	
//	@Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//
//        // 🔹 OPTION 2: Typed serializer for TicketResponse
//        Jackson2JsonRedisSerializer<TicketResponse> serializer =
//                new Jackson2JsonRedisSerializer<>(TicketResponse.class);
//        serializer.setObjectMapper(mapper);
//
//        RedisCacheConfiguration config =
//                RedisCacheConfiguration.defaultCacheConfig()
//                        .serializeKeysWith(
//                                RedisSerializationContext.SerializationPair
//                                        .fromSerializer(new StringRedisSerializer())
//                        )
//                        .serializeValuesWith(
//                                RedisSerializationContext.SerializationPair
//                                        .fromSerializer(serializer)
//                        );
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
	
	
	
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(new JavaTimeModule());
//
//	    // Serializer for single TicketResponse
//	    Jackson2JsonRedisSerializer<TicketResponse> ticketSerializer =
//	            new Jackson2JsonRedisSerializer<>(TicketResponse.class);
//	    ticketSerializer.setObjectMapper(mapper);
//
//	    // Generic serializer for List<TicketResponse>
//	    GenericJackson2JsonRedisSerializer genericSerializer =
//	            new GenericJackson2JsonRedisSerializer(mapper);
//
//	    // Cache config for single ticket
//	    RedisCacheConfiguration ticketByIdConfig =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(new StringRedisSerializer())
//	                    )
//	                    .serializeValuesWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(ticketSerializer)
//	                    );
//
//	    // Cache config for list
//	    RedisCacheConfiguration ticketsConfig =
//	            RedisCacheConfiguration.defaultCacheConfig()
//	                    .serializeKeysWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(new StringRedisSerializer())
//	                    )
//	                    .serializeValuesWith(
//	                            RedisSerializationContext.SerializationPair
//	                                    .fromSerializer(genericSerializer)
//	                    );
//
//	    return RedisCacheManager.builder(connectionFactory)
//	            .withCacheConfiguration("ticketById", ticketByIdConfig)
//	            .withCacheConfiguration("tickets", ticketsConfig)
//	            .build();
//	}


}
