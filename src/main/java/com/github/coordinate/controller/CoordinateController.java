package com.github.coordinate.controller;

import com.github.coordinate.common.web.JsonResult;
import com.github.coordinate.util.CoordTransformUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


public class CoordinateController {
    @PostMapping("/coordTest")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "B1", value = "纬度1", required = true, dataType = "Double"),
            @ApiImplicitParam(paramType = "query", name = "L1", value = "经度1", required = true, dataType = "Double"),
            @ApiImplicitParam(paramType = "query", name = "H1", value = "高程1", required = false, dataType = "Double"),
            @ApiImplicitParam(paramType = "query", name = "B2", value = "纬度2", required = true, dataType = "Double"),
            @ApiImplicitParam(paramType = "query", name = "L2", value = "经度2", required = true, dataType = "Double"),
            @ApiImplicitParam(paramType = "query", name = "H2", value = "高程2", required = false, dataType = "Double")
    })
    public @ResponseBody
    JsonResult coordTest(Double B1, Double L1, Double H1, Double B2, Double L2, Double H2) {

        if (H1 == null) {
            H1 = 0.0;
        }
        if (H2 == null) {
            H2 = 0.0;
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> tempMap = new HashMap<String, Object>();

        tempMap = CoordTransformUtils.BLHtoXYZ(B1, L1, H1);
        resultMap.put("X1", tempMap.get("X"));
        resultMap.put("Y1", tempMap.get("Y"));

        tempMap = CoordTransformUtils.BLHtoXYZ(B2, L2, H2);
        resultMap.put("X2", tempMap.get("X"));
        resultMap.put("Y2", tempMap.get("Y"));

        return JsonResult.success(resultMap);
    }
}
