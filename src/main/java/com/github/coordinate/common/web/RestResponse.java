package com.github.coordinate.common.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.UUID;


@ApiModel(description = "响应消息体")
@Data   //lombok plugins
public class RestResponse implements Serializable {
	public final static String SUCCESS ="200";
	public final static String FAILE ="505";
	
	   /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 描述 : 响应ID
     */
    @ApiModelProperty(value = "响应ID", required = true, dataType = "string")
    private String ID = UUID.randomUUID().toString();

    /**
     * 描述 : 状态码(业务定义)
     */
    @ApiModelProperty(value = "状态码(业务定义)", required = true, dataType = "string")
    private String result = Integer.toString(HttpStatus.OK.value());

    /**
     * 描述 : 状态码描述(业务定义)
     */
    @ApiModelProperty(value = "状态码描述(业务定义)", required = true, dataType = "string")
    private String msg = "";

    /**
     * 描述 : 结果集(泛型)
     */
    @ApiModelProperty(value = "结果集(泛型)", required = true, dataType = "object")
    private Object data = null; //NOSONAR

    /**
     * 描述 : 错误
     */
    @ApiModelProperty(value = "错误", required = true, dataType = "object")
    private Exception error = null;
    
    /**
     * 描述 : 构造函数
     */
    public RestResponse() {
        super();
    }

    /**
     * 描述 : 构造函数
     *
     * @param data 结果集(泛型)
     */
    public RestResponse(Object data) {
        super();
        this.data = data;
    }

    /**
     * 描述 : 构造函数
     *
     * @param httpStatus http状态
     * @param error      错误
     */
    public RestResponse(HttpStatus httpStatus, Exception error) {
        super();
        this.result = Integer.toString(httpStatus.value());
        this.msg = httpStatus.getReasonPhrase();
        this.error = error;
    }

    /**
     * 描述 : 构造函数
     *
     * @param result    状态码(业务定义)
     * @param msg 状态码描述(业务定义)
     * @param error   错误
     */
    public RestResponse(String result, String msg, Exception error) {
        super();
        this.result = result;
        this.msg = msg;
        this.error = error;
    }

    /**
     * 描述 : 构造函数
     *
     * @param result    状态码(业务定义)
     * @param msg 状态码描述(业务定义)
     */
    public RestResponse(String result, String msg) {
        super();
        this.result = result;
        this.msg = msg;
    }

    /**
     * 描述 : 构造函数
     *
     * @param result    状态码(业务定义)
     * @param msg 状态码描述(业务定义)
     * @param result  结果集(泛型)
     */
    public RestResponse(String result, String msg, Object data) {
        super();
        this.result = result;
        this.msg = msg;
        this.data = data;
    }
}
