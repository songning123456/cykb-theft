package com.sn.theft.constant;

/**
 * @author sn
 */
public class HttpStatus {
    /**
     * [GET] 服务器成功返回用户请求的数据，该操作是幂等的
     */
    public static final int HTTP_OK = 200;
    /**
     * [POST/PUT/PATCH] 用户新建或修改数据成功
     */
    public static final int HTTP_CREATED = 201;
    /**
     * [*] 表示一个请求已经进入后台排队（异步任务）
     */
    public static final int HTTP_ACCEPTED = 202;
    public static final int HTTP_NOT_AUTHORITATIVE = 203;
    /**
     * [DELETE] 用户删除数据成功
     */
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_RESET = 205;
    public static final int HTTP_PARTIAL = 206;
    public static final int HTTP_MULT_CHOICE = 300;
    public static final int HTTP_MOVED_PERM = 301;
    public static final int HTTP_MOVED_TEMP = 302;
    public static final int HTTP_SEE_OTHER = 303;
    public static final int HTTP_NOT_MODIFIED = 304;
    public static final int HTTP_USE_PROXY = 305;
    /**
     * [POST/PUT/PATCH]	用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的
     */
    public static final int HTTP_BAD_REQUEST = 400;
    /**
     * [*] 表示用户没有权限（令牌、用户名、密码错误）
     */
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_PAYMENT_REQUIRED = 402;
    /**
     * [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的
     */
    public static final int HTTP_FORBIDDEN = 403;
    /**
     * [*] 用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的
     */
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_BAD_METHOD = 405;
    /**
     * [GET] 用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）
     */
    public static final int HTTP_NOT_ACCEPTABLE = 406;
    public static final int HTTP_PROXY_AUTH = 407;
    public static final int HTTP_CLIENT_TIMEOUT = 408;
    public static final int HTTP_CONFLICT = 409;
    /**
     * [GET] 用户请求的资源被永久删除，且不会再得到的
     */
    public static final int HTTP_GONE = 410;
    public static final int HTTP_LENGTH_REQUIRED = 411;
    public static final int HTTP_PRECON_FAILED = 412;
    public static final int HTTP_ENTITY_TOO_LARGE = 413;
    public static final int HTTP_REQ_TOO_LONG = 414;
    public static final int HTTP_UNSUPPORTED_TYPE = 415;
    /**
     * [*] 服务器发生错误，用户将无法判断发出的请求是否成功
     */
    public static final int HTTP_INTERNAL_ERROR = 500;
    public static final int HTTP_NOT_IMPLEMENTED = 501;
    public static final int HTTP_BAD_GATEWAY = 502;
    public static final int HTTP_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;
    public static final int HTTP_VERSION = 505;

    private HttpStatus() {
    }
}
