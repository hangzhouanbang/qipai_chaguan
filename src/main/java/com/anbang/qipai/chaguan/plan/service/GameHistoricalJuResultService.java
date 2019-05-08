package com.anbang.qipai.chaguan.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.chaguan.plan.bean.MemberDayHistoricalResult;
import com.anbang.qipai.chaguan.plan.bean.MemberDayResult;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;
import com.anbang.qipai.chaguan.plan.dao.GameHistoricalJuResultDao;
import com.anbang.qipai.chaguan.plan.dao.MemberDayHistoricalResultDao;
import com.anbang.qipai.chaguan.plan.dao.MemberDayResultDao;
import com.anbang.qipai.chaguan.util.TimeUtil;
import com.highto.framework.web.page.ListPage;

@Service
public class GameHistoricalJuResultService {

	@Autowired
	private GameHistoricalJuResultDao majiangHistoricalResultDao;

	@Autowired
	private MemberDayHistoricalResultDao memberDayHistoricalResultDao;

	@Autowired
	private MemberDayResultDao memberDayResultDao;

	@Autowired
	private MemberDboDao memberDboDao;

	public void addGameHistoricalResult(GameHistoricalJuResult result) {
		majiangHistoricalResultDao.addGameHistoricalResult(result);
	}

	public void updateIncMemberDayResult(String playerId, String chaguanId, int dayingjiaCount, int chaguanYushiCost,
			int totalScore, long createTime) {
		long startTime = TimeUtil.getDayStartTime(createTime);
		long endTime = TimeUtil.getDayEndTime(createTime);
		MemberDayResult result = memberDayResultDao.findByPlayerIdAndTime(playerId, startTime, endTime);
		if (result != null) {
			memberDayResultDao.updateIncById(result.getId(), dayingjiaCount, chaguanYushiCost, totalScore, createTime);
		} else {
			MemberDbo member = memberDboDao.findById(playerId);
			result = new MemberDayResult();
			result.setPlayerId(playerId);
			result.setNickname(member.getNickname());
			result.setHeadimgurl(member.getHeadimgurl());
			result.setChaguanId(chaguanId);
			result.setDayingjiaCount(dayingjiaCount);
			result.setChaguanYushiCost(chaguanYushiCost);
			result.setTotalScore(totalScore);
			result.setCreateTime(createTime);
			memberDayResultDao.save(result);
		}
	}

	public void finishMemberDayResultById(String id) {
		MemberDayResult result = memberDayResultDao.findById(id);
		long startTime = TimeUtil.getDayStartTime(result.getCreateTime());
		long endTime = TimeUtil.getDayEndTime(result.getCreateTime());
		MemberDayHistoricalResult hr = memberDayHistoricalResultDao.findByPlayerIdAndTime(result.getPlayerId(),
				startTime, endTime);
		if (hr != null) {
			memberDayHistoricalResultDao.updateIncById(hr.getId(), result.getDayingjiaCount(),
					result.getChaguanYushiCost(), result.getTotalScore(), System.currentTimeMillis());
		} else {
			hr = new MemberDayHistoricalResult();
			hr.setPlayerId(result.getPlayerId());
			hr.setNickname(result.getNickname());
			hr.setHeadimgurl(result.getHeadimgurl());
			hr.setChaguanId(result.getChaguanId());
			hr.setChaguanYushiCost(result.getChaguanYushiCost());
			hr.setDayingjiaCount(result.getDayingjiaCount());
			hr.setTotalScore(result.getTotalScore());
			hr.setCreateTime(System.currentTimeMillis());
			memberDayHistoricalResultDao.save(hr);
		}
		memberDayResultDao.removeById(id);
	}

	public ListPage findGameHistoricalResultByMemberId(int page, int size, String memberId) {
		long amount = majiangHistoricalResultDao.getAmountByMemberId(memberId);
		List<GameHistoricalJuResult> list = majiangHistoricalResultDao.findGameHistoricalResultByMemberId(page, size,
				memberId);
		ListPage listPage = new ListPage(list, page, size, (int) amount);
		return listPage;
	}

	public ListPage findGameHistoricalResultByMemberIdAndChaguanId(long startTime, long endTime, int page, int size,
			String memberId, String chaguanId) {
		long amount = majiangHistoricalResultDao.getAmountByMemberIdAndChaguanIdAndAndRoomNoTime(chaguanId, memberId,
				null, startTime, endTime);
		List<GameHistoricalJuResult> list = majiangHistoricalResultDao
				.findGameHistoricalResultByMemberIdAndChaguanIdAndRoomNoAndTime(page, size, chaguanId, memberId, null,
						startTime, endTime);
		ListPage listPage = new ListPage(list, page, size, (int) amount);
		return listPage;
	}

	public ListPage findMemberDayResultByTimeAndChaguanId(long startTime, long endTime, int page, int size,
			String chaguanId) {
		long amount = memberDayResultDao.countByChaguanIdAndTime(chaguanId, startTime, endTime);
		List<MemberDayResult> list = memberDayResultDao.findByChaguanIdAndTime(page, size, chaguanId, startTime,
				endTime);
		ListPage listPage = new ListPage(list, page, size, (int) amount);
		return listPage;
	}

	public ListPage findMemberDayHistoricalResultByTimeAndChaguanId(long startTime, long endTime, int page, int size,
			String chaguanId) {
		long amount = memberDayHistoricalResultDao.countByChaguanIdAndTime(chaguanId, startTime, endTime);
		List<MemberDayHistoricalResult> list = memberDayHistoricalResultDao.findByChaguanIdAndTime(page, size,
				chaguanId, startTime, endTime);
		ListPage listPage = new ListPage(list, page, size, (int) amount);
		return listPage;
	}

	public GameHistoricalJuResult findGameHistoricalResultById(String id) {
		return majiangHistoricalResultDao.findGameHistoricalResultById(id);
	}

	public int countGameNumByGameAndTime(Game game, long startTime, long endTime) {
		return majiangHistoricalResultDao.countGameNumByGameAndTime(game, startTime, endTime);
	}
}
