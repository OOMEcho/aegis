package com.aegis.common.excel;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.WriteConverterContext;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.data.WriteCellData;
import com.aegis.common.constant.CommonConstants;

/**
 * 操作状态转换类
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
public class StatusConvert implements Converter<String> {

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) {
        if (CommonConstants.NORMAL_STATUS.equals(context.getValue())) {
            return new WriteCellData<>("成功");
        }
        return new WriteCellData<>("失败");
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
