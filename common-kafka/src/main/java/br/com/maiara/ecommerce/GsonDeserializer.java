package br.com.maiara.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T>  implements Deserializer<T>  {

    public static final String TYPE_CONFIG = "br.com.maiara.ecommerce.type_config";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type;

    //método recebe as properties do kafka (properties.setProperty)
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
        try {
            this.type = (Class<T>) Class.forName(typeName);
        } catch(ClassNotFoundException e){
            throw new RuntimeException("Type deserializable does not exist. " + e);
        }
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        return gson.fromJson(new String(bytes), type);
    }
}
