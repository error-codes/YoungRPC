package io.young.rpc.provider.common.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.WrongArgumentException;
import io.young.rpc.common.exception.YoungAssert;
import io.young.rpc.common.help.ServiceHelper;
import io.young.rpc.common.threadpool.ServerThreadPool;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.constants.YoungConstants;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.enumeration.RpcStatus;
import io.young.rpc.protocol.enumeration.RpcType;
import io.young.rpc.protocol.header.YoungHeader;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.protocol.response.YoungResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author YoungCR
 * @date 2024/12/18 19:06
 * @descritpion RpcProviderHandler 服务提供者处理器
 */
public class YoungProviderHandler extends SimpleChannelInboundHandler<YoungProtocol<YoungRequest>> {

    private static final Logger logger = LoggerFactory.getLogger(YoungProviderHandler.class);

    private final Map<String, Object> handlerMap;
    private final String              reflectType;

    public YoungProviderHandler(Map<String, Object> handlerMap, String reflectType) {
        this.handlerMap = handlerMap;
        this.reflectType = reflectType;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, YoungProtocol<YoungRequest> requestYoungProtocol) {
        ServerThreadPool.submit(() -> {
            YoungHeader header = requestYoungProtocol.getHeader();
            header.setPacketType(RpcType.RESPONSE.getType());
            YoungRequest request = requestYoungProtocol.getBody();
            logger.debug("Receive request {}", header.getRequestId());
            YoungProtocol<YoungResponse> responseYoungProtocol = new YoungProtocol<>();
            YoungResponse                response              = new YoungResponse();
            try {
                Object result = handle(request);
                response.setResult(result);
                response.async(request.isAsync());
                response.oneway(request.isOneway());
                header.setStatus(RpcStatus.SUCCESS.getCode());
            } catch (Exception e) {
                response.setError(e.toString());
                header.setStatus(RpcStatus.FAILURE.getCode());
                logger.error("RPC Server handle request error", e);
            }
            responseYoungProtocol.setHeader(header);
            responseYoungProtocol.setBody(response);
            channelHandlerContext.writeAndFlush(responseYoungProtocol).addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    logger.info("Response successfully sent for request {}", header.getRequestId());
                } else {
                    logger.info("Failed to send response for request {}: {}", header.getRequestId(), channelFuture.cause().getMessage());
                }
            });
        });
    }

    private Object handle(YoungRequest request) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String serviceKey  = ServiceHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
        Object serviceBean = handlerMap.get(serviceKey);

        YoungAssert.throwAssertIfNullOrEmpty(serviceBean, RespUtils.getContent("Service.0", request.getClassName(), request.getMethodName()));

        Class<?>   serviceClass   = serviceBean.getClass();
        String     methodName     = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[]   parameters     = request.getParameters();

        logger.debug(serviceClass.getName());
        logger.debug(methodName);
        if (parameterTypes != null) {
            for (Class<?> parameterType : parameterTypes) {
                logger.debug(parameterType.getName());
            }
        }

        if (parameters != null) {
            for (Object parameter : parameters) {
                logger.debug(parameter.toString());
            }
        }
        return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
    }

    private Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        switch (this.reflectType) {
            case YoungConstants.REFLECT_TYPE_JDK -> {
                return this.invokeJdkMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            }
            case YoungConstants.REFLECT_TYPE_CGLIB -> {
                return this.invokeCglibMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            }
            default -> throw ExceptionFactory.createException(WrongArgumentException.class, "not support reflect type");
        }
    }

    private Object invokeJdkMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.info("use jdk reflect type invoke method.");
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    private Object invokeCglibMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws InvocationTargetException {
        logger.info("use cglib reflect type invoke method.");
        FastClass  serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceMethod    = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("server caught exception", cause);
    }
}
