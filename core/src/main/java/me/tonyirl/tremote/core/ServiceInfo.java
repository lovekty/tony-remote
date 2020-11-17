package me.tonyirl.tremote.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author tony.zhuby
 * @date 2020/5/21
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public final class ServiceInfo implements Serializable {
    private static final long serialVersionUID = -5989624680059267365L;
    @NonNull
    private String name;
    @NonNull
    private Integer port = 0;
    @NonNull
    private String version = "";

    public final String defineName() {
        return StringUtils.isBlank(version) ? name : name + "@" + version;
    }
}
