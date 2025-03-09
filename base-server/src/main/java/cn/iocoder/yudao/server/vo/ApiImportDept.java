package cn.iocoder.yudao.server.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExcelIgnoreUnannotated
@Accessors(chain = false)
public class ApiImportDept {
    @ExcelProperty(value = "上级工会")
    private String parentName;

    @ExcelProperty(value = "下级工会")
    private String deptName;

    @ExcelProperty(value = "所在区")
    private String areaName;
}
