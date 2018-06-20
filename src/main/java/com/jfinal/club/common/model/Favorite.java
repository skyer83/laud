/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

package com.jfinal.club.common.model;

import com.jfinal.club.common.model.base.BaseFavorite;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Favorite extends BaseFavorite<Favorite> {

    // 收藏引用的资源类型
    public static final int REF_TYPE_PROJECT = 1;
    public static final int REF_TYPE_SHARE = 2;
    public static final int REF_TYPE_FEEDBACK = 3;

    private static Map<String, Integer> tableToTypeValue = new HashMap<String, Integer>(){{
        put("project", REF_TYPE_PROJECT);
        put("share", REF_TYPE_SHARE);
        put("feedback", REF_TYPE_FEEDBACK);
    }};

    private static Map<Integer, String> typeValueToTable = new HashMap<Integer, String>(){{
        put(REF_TYPE_PROJECT, "project");
        put(REF_TYPE_SHARE, "share");
        put(REF_TYPE_FEEDBACK, "feedback");
    }};

    public static String getRefTable(int refType) {
        return typeValueToTable.get(refType);
    }

    public static int getRefType(String tableName) {
        Integer refType = tableToTypeValue.get(tableName);
        if (refType == null) {
            throw new IllegalArgumentException("tableName 不正确");
        }
        return refType;
    }

    public static void checkRefTypeTable(String refTypeTable) {
        if ( !tableToTypeValue.containsKey(refTypeTable) ) {
            throw new IllegalArgumentException("refType 不正确");
        }
    }

    /**
     * 返回收藏资源的 url
     */
    public String getRefUrl() {
        int refType = getRefType();
        int refId = getRefId();
        if (refType == REF_TYPE_PROJECT) {
            return "/project/" + refId;
        } else if (refType == REF_TYPE_SHARE) {
            return "/share/" + refId;
        } else if (refType == REF_TYPE_FEEDBACK) {
            return "/feedback/" + refId;
        } else {
            throw new RuntimeException("refType 不正确，无法生成 url，reType = " + refType);
        }
    }
}
