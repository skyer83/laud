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

package com.jfinal.club._admin.share;

import com.jfinal.aop.Before;
import com.jfinal.club.my.share.MyShareValidator;
import com.jfinal.club.project.ProjectService;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.club.common.controller.BaseController;
import com.jfinal.kit.Ret;
import com.jfinal.club.common.model.Share;
import com.jfinal.club.common.model.ShareReply;
import com.jfinal.club.index.IndexService;
import com.jfinal.club.share.ShareService;
import java.util.List;

/**
 * 分享管理控制器
 */
public class ShareAdminController extends BaseController {

	ShareAdminService srv = ShareAdminService.me;

	public void index() {
		Page<Share> sharePage = srv.paginate(getParaToInt("p", 1));
		setAttr("sharePage", sharePage);
		render("index.html");
	}

	/**
	 * 修改
	 */
	public void edit() {
		setAttr("projectList", ProjectService.me.getAllProject("id, name"));    // 关联项目下拉列表
		setAttr("share", srv.edit(getParaToInt("id")));
		render("edit.html");
	}

	/**
	 * 提交修改
	 */
	@Before(MyShareValidator.class)
	public void update() {
		Share share = getBean(Share.class);
		Ret ret = srv.update(share);
		renderJson(ret);
	}

	/**
	 * 锁定
	 */
	public void lock() {
		Ret ret = srv.lock(getParaToInt("id"));

		ShareService.me.clearHotShareCache();
		IndexService.me.clearCache();
		renderJson(ret);
	}

	/**
	 * 解除锁定
	 */
	public void unlock() {
		Ret ret = srv.unlock(getParaToInt("id"));
		
		ShareService.me.clearHotShareCache();
		IndexService.me.clearCache();
		renderJson(ret);
	}

	/**
	 * 删除 share
	 */
	public void delete() {
		Ret ret = srv.delete(getParaToInt("id"));
		renderJson(ret);
	}

	/**
	 * 显示 share reply 列表
	 */
	public void replyList() {
		int shareId = getParaToInt("shareId");
		List<ShareReply> shareReplyList = srv.getReplyList(shareId);
		setAttr("shareReplyList", shareReplyList);
		setAttr("shareId", shareId);
		render("reply.html");
	}

	/**
	 * 删除 share reply
	 */
	public void deleteReply() {
		int replyId = getParaToInt("replyId");
		srv.deleteReply(replyId);
		renderJson(Ret.ok("msg", "share reply 删除成功"));
	}
}



