package me.dmk.core.database.data;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import lombok.RequiredArgsConstructor;
import me.dmk.core.database.MongoClientService;
import me.dmk.core.database.data.entity.DataEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by DMK on 10.02.2023
 */

@RequiredArgsConstructor
public class MongoDataService {

    private final Logger logger;
    private final MongoClientService mongoClientService;
    private final Gson gson;

    private final Map<Class<?>, String> collections = new ConcurrentHashMap<>();

    public MongoCollection<Document> getCollection(Class<?> clazz) {
        String collection = this.collections.computeIfAbsent(
                clazz, c -> clazz.isAnnotationPresent(DataEntity.class) ? clazz.getAnnotation(DataEntity.class).collection() : clazz.getSimpleName()
        );

        return this.mongoClientService.getMongoDatabase().getCollection(collection);
    }

    public <V> void insert(V entity) {
        MongoCollection<Document> mongoCollection = this.getCollection(entity.getClass());

        if (mongoCollection == null) {
            logger.severe("Cannot find collection from class " + entity.getClass().getSimpleName() + ", check entity annotation.");
            return;
        }

        String json = this.gson.toJson(entity);
        Document document = Document.parse(json);

        if (document == null) {
            logger.severe("Error while trying to parse document from json: " + json);
            return;
        }

        mongoCollection.insertOne(document);
    }

    public <V> Optional<V> find(Bson filters, Class<V> vClass) {
        MongoCollection<Document> mongoCollection = this.getCollection(vClass);

        if (mongoCollection == null) {
            logger.severe("Cannot find collection from class " + vClass.getSimpleName() + ", check entity annotation.");
            return Optional.empty();
        }

        Document document = mongoCollection.find(filters).first();

        if (document == null) {
            return Optional.empty();
        }

        String json = document.toJson();
        V entity = this.gson.fromJson(json, vClass);

        return Optional.ofNullable(entity);
    }

    public <V> void save(Bson filters, V entity) {
        MongoCollection<Document> mongoCollection = this.getCollection(entity.getClass());

        if (mongoCollection == null) {
            logger.severe("Cannot find collection from class " + entity.getClass().getSimpleName() + ", check entity annotation.");
            return;
        }

        String json = this.gson.toJson(entity);
        Document document = Document.parse(json);

        if (document == null) {
            logger.severe("Error while trying to parse document from json: " + json);
            return;
        }

        mongoCollection.replaceOne(filters, document, new ReplaceOptions().upsert(true));
    }

    public <V> void delete(V vEntity) {
        String json = this.gson.toJson(vEntity);
        Document document = Document.parse(json);

        if (document == null) {
            logger.severe("Error while trying to parse document from json: " + json);
            return;
        }

        MongoCollection<Document> mongoCollection = this.getCollection(vEntity.getClass());

        if (mongoCollection == null) {
            logger.severe("Cannot find collection from class " + vEntity.getClass().getSimpleName() + ", check entity annotation.");
            return;
        }

        mongoCollection.deleteOne(document);
    }

    public <V> List<V> sort(Class<V> vClass, Bson sort, int limit) {
        MongoCollection<Document> mongoCollection = this.getCollection(vClass);

        return mongoCollection.find()
                .sort(sort)
                .limit(limit)
                .into(new ArrayList<>())
                .stream()
                .map(document -> this.gson.fromJson(document.toJson(), vClass))
                .collect(Collectors.toList());
    }
}
