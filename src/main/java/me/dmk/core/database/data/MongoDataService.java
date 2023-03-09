package me.dmk.core.database.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.ReplaceOptions;
import lombok.RequiredArgsConstructor;
import me.dmk.core.database.MongoClientService;
import me.dmk.core.database.data.entity.DataEntity;
import me.dmk.core.database.data.serializer.GsonSerializer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by DMK on 10.02.2023
 */

@RequiredArgsConstructor
public class MongoDataService {

    private final Logger logger;
    private final GsonSerializer gsonSerializer;
    private final MongoClientService mongoClientService;

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

        Document document = Document.parse(
                this.gsonSerializer.serialize(entity)
        );

        mongoCollection.insertOne(document);
    }

    public <V> void save(Bson filters, V entity) {
        MongoCollection<Document> mongoCollection = this.getCollection(entity.getClass());

        if (mongoCollection == null) {
            logger.severe("Cannot find collection from class " + entity.getClass().getSimpleName() + ", check entity annotation.");
            return;
        }

        Document document = Document.parse(
                this.gsonSerializer.serialize(entity)
        );

        mongoCollection.replaceOne(filters, document, new ReplaceOptions().upsert(true));
    }

    public <V> void delete(Bson filters, V entity) {
        Document document = Document.parse(
                this.gsonSerializer.serialize(entity)
        );

        if (document == null) {
            logger.severe("Error while trying to parse document from class: " + entity.toString());
            return;
        }

        MongoCollection<Document> mongoCollection = this.getCollection(entity.getClass());

        if (mongoCollection == null) {
            logger.severe("Cannot find collection from class " + entity.getClass().getSimpleName() + ", check entity annotation.");
            return;
        }

        mongoCollection.deleteOne(filters, new DeleteOptions());
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

        return Optional.ofNullable(
                this.gsonSerializer.deserialize(document.toJson(), vClass)
        );
    }

    public <V> List<V> findAll(Bson filters, Class<V> vClass) {
        MongoCollection<Document> mongoCollection = this.getCollection(vClass);

        if (mongoCollection == null) {
            logger.severe("Cannot find collection from class " + vClass.getSimpleName() + ", check entity annotation.");
            return Collections.emptyList();
        }

        return mongoCollection.find(filters)
                .map(document -> this.gsonSerializer.deserialize(document.toJson(), vClass))
                .into(new ArrayList<>());
    }

    public <V> List<V> sort(Class<V> vClass, Bson sort, int limit) {
        MongoCollection<Document> mongoCollection = this.getCollection(vClass);

        return mongoCollection.find()
                .sort(sort)
                .limit(limit)
                .into(new ArrayList<>())
                .stream()
                .map(document -> this.gsonSerializer.deserialize(document.toJson(), vClass))
                .collect(Collectors.toList());
    }
}
