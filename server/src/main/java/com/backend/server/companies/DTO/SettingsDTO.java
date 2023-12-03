package com.backend.server.companies.DTO;



import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {

    // json muodossa uudet settings companylle
    // companyssä field on 
    /*  @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "settings_json", columnDefinition = "jsonb", nullable = true)
    private Map<String, Object> settings; */
    // joten tässäkin pitää olla sama formaatti
    // esim. {"field1": "value1", "field2": "value2", "field3": "value3"}

    private String companyName;
    private Map<String, Object> settings;
    
}
