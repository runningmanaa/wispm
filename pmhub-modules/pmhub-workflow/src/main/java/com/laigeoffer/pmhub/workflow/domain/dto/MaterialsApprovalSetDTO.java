package com.laigeoffer.pmhub.workflow.domain.dto;

import lombok.Data;

/**
 * @author zw
 * @date 2023-04-20 13:44
 */
@Data
public class MaterialsApprovalSetDTO {

    private String type;
    private String approved;
    private String definitionId;
    private String deploymentId;

}
