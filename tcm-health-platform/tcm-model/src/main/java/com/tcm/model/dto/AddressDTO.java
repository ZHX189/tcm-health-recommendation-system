package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 地址DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "收货地址请求")
public class AddressDTO {

    @Schema(description = "收货人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名最多50个字符")
    private String receiverName;

    @Schema(description = "收货人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;

    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "省份不能为空")
    private String province;

    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "城市不能为空")
    private String city;

    @Schema(description = "区/县", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区/县不能为空")
    private String district;

    @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 255, message = "详细地址最多255个字符")
    private String detailAddress;

    @Schema(description = "是否默认：0-否，1-是")
    private Integer isDefault;
}
