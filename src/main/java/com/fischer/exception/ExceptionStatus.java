package com.fischer.exception;

import lombok.Getter;


/**
 * @author fischer
 */
@Getter
public enum ExceptionStatus {
    /*基本异常处理信息*/
    SUCCESS(200,"OK"),
    /*参数校验失败*/
    BAD_REQUEST(400,"参数校验失败，请重新填写"),

    /*服务器错误*/
    INTERNAL_SERVER_ERROR(500, "服务器异常，请联系管理员"),

    ERROR_ADD_ARTICLE_FAIL(10001,"新增文章失败"),

    ERROR_DELETE_ARTICLE_FAIL(10002,"删除文章失败"),

    ERROR_EDIT_ARTICLE_FAIL(10003,"修改文章失败"),

    ERROR_COUNT_ARTICLE_FAIL(10004,"统计文章数量失败"),

    ERROR_GET_ARTICLES_FAIL(10005,"获取多个文章失败"),

    ERROR_GET_ARTICLE_FAIL(10006,"当前要获取的文章不存在或已被删除"),

    ERROR_GEN_ARTICLE_POSTER_FAIL(10007,"生成文章海报失败"),

    ERROR_LIKE(10008,"已经为点赞状态"),

    ERROR_DISLIKE(10009,"已经为取消点赞的状态"),

    ERROR_NO_COMMENT(10010,"当前要评论的文章不存在"),

    ERROR_LIKE_FAIL(10011,"点赞文章失败"),

    ERROR_DISLIKE_FAIL(10012,"取消点赞失败"),

    ERROR_CREATE_COMMENT_FAIL(10013,"创建评论失败"),

    ERROR_DELETE_COMMENT_FAIL(10014,"删除评论失败"),

    ERROR_LIKE_COMMENT_FAIL(10015,"点赞评论失败"),

    ERROR_DISLIKE_COMMENT_FAIL(10016,"取消点赞评论失败"),

    ERROR_NO_COMMENT_REPLY(10017,"删除子评论失败，当前删除的子评论可能已经不存在"),

    ERROR_CREATE_REPLY(10018,"创建子评论失败"),

    ERROR_DELETE_REPLY(10019,"删除子评论失败"),

    ERROR_LIKE_COMMENT_REPLY(10020,"点赞子评论失败"),

    ERROR_DISLIKE_COMMENT_REPLY(10021,"取消点赞子评论失败"),


    ERROR_NOT_LOGIN(20001,"未登录，请重新登录"),

    ERROR_NOT_AUTH(20002,"您当前无权限进行该操作"),

    ERROR_BLOCK(20003,"当前用户操作受限"),

    ERROR_FREQUENCY(20004,"请求频率过快"),

    ERROR_CREATE_USER_FAIL(20005,"用户创建失败"),

    ERROR_GET_USER_FAIL(20006,"获取用户详情失败"),

    ERROR_UPDATE_USER_FAIL(20007,"更新用户详情失败"),


    ERROR_FILE(30001,"上传文件失败"),

    ERROR_MAX_FILE(30002,"当前文件格式不支持，请上传以 .jpg .png .jpeg .webp .gif结尾的图片")


    ;

    private final int value;
    private final String reason;
    ExceptionStatus(int value, String reason) {
        this.reason = reason;
        this.value = value;

    }
}
