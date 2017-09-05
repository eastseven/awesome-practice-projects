package ${packageName};

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ${className} {

    public static final String TABLE_NAME = "${tableName}";
    public static final String FAMILY_NAME = "f";

    private String id;

    <#list properties as property>
    private ${property.type} ${property.name};
    </#list>

}