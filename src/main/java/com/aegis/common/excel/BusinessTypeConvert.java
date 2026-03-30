package com.aegis.common.excel;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.WriteConverterContext;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.data.WriteCellData;

/**
 * 业务类型转换类
 *
 * @author xuesong.lei
 * @since 2025/9/7 21:23
 */
public class BusinessTypeConvert implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
        switch (context.getValue()) {
            case 1:
                return new WriteCellData<>("新增");
            case 2:
                return new WriteCellData<>("修改");
            case 3:
                return new WriteCellData<>("删除");
            case 4:
                return new WriteCellData<>("导出");
            case 5:
                return new WriteCellData<>("导入");
            default:
                return new WriteCellData<>("其他");
        }
    }

    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }
}
