package com.github.coordinate.common.web;

/**
 * Created with IntelliJ IDEA.
 * User: gld
 * Date: 2017/12/11
 * Time: 15:01
 */
/**
 * @author xzs xuzeshen@makepower.cc
 * @ClassName: JsonResult
 * @Description: Http请求返回的Json结果
 * @date 2016年12月27日 下午3:19:24
 */
public class JsonResult {
    /**
     * 请求结果，默认为success
     */
    private String result = SUCCESS;
    /**
     * 请求结果描述，一般当resul不为"success"时，填充该值
     */
    private String msg;
    /**
     * 结果集
     */
    private Object data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static JsonResult success(Object data) {
        return success(null, data);
    }

    public static JsonResult success(String msg, Object data) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(data);
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    public static JsonResult successMsg(String msg) {

        return success(null,msg);
    }
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public static JsonResult fail(String msg) {
        return fail(msg, null);
    }

    public static JsonResult fail(String msg, Object data) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(data);
        jsonResult.setMsg(msg);
        jsonResult.setResult(FAIL);
        return jsonResult;
    }
}
