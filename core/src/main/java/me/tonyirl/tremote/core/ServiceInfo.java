package me.tonyirl.tremote.core;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * @author tony.zhuby
 * @date 2020/5/21
 */
@Data
public final class ServiceInfo implements Serializable {
    private static final long serialVersionUID = -5989624680059267365L;
    @NonNull
    private String name;
    @NonNull
    private Integer port = 0;
    @NonNull
    private String version = "";

    @JsonCreator
    public ServiceInfo(@JsonProperty("name") @NonNull String name) {
        this.name = name;
    }

    public final String defineName() {
        return StringUtils.isBlank(version) ? name : name + "@" + version;
    }
}
