package com.yibai.crown.web.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yibai.crown.provider.exception.BizException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ExceptionErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                JSONObject jsonObject = JSON.parseObject(Util.toString(response.body().asReader()));
                Class clazz = Class.forName(jsonObject.get("exception").toString());
                String message = jsonObject.get("message").toString();
                if(clazz.isInstance(new BizException())){
                    String [] messages =  message.split(",");
                    BizException bizException = (BizException) clazz.newInstance();
                    bizException.setErrorCode(messages[0]);
                    bizException.setMessage(messages[1]);
                    return bizException;
                }
                return (Exception) clazz.getDeclaredConstructor(String.class).newInstance(jsonObject.get("message").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return FeignException.errorStatus(methodKey, response);
    }
}
