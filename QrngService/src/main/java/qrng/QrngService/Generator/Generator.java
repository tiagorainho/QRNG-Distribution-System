package qrng.QrngService.Generator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Generator {
    @Id
    @JsonIgnore
    private String id;
    @Indexed(unique=true)
    private String name;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String url;
    private GeneratorType type;
    //private Queue<Byte> randomBits;
    @JsonIgnore
    private long cacheSize;
    // length of each element from the array returned
    @JsonIgnore
    private int elementSize;
    
    public Generator(String name, String url, GeneratorType type, long cacheSize) {
        this.name = name;
        this.url = url;
        this.type = type;
        //this.randomBits = new ConcurrentLinkedQueue<>();
        this.cacheSize = cacheSize;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; } 
    public String getUrl() { return this.url; }
    public GeneratorType getType() { return this.type; }
    public long getCacheSize() { return this.cacheSize; }
    public int getElementSize() { return this.elementSize; }
    //public Queue<Byte> getRandomBits() { return this.randomBits; }


    public void setName(String name) { this.name = name; }
    public void setUrl(String url) { this.url = url; }
    public void setType(GeneratorType type) { this.type = type; }
    public void setCacheSize(long cacheSize) { this.cacheSize = cacheSize; }
    public void setElementSize(int elementSize) { this.elementSize = elementSize; }

}
