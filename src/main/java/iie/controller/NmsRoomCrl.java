package iie.controller;

import com.alibaba.druid.util.StringUtils;
import iie.domain.AlarmBean;
import iie.domain.AssetMessageBean;
import iie.pojo.NmsAlarm;
import iie.pojo.NmsAsset;
import iie.service.NmsAssetService;
import iie.service.NmsNettopoService;
import iie.service.NmsXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/nmsx")
public class NmsRoomCrl {
	@Autowired
	NmsXService nmsXService;

	@Autowired
	NmsNettopoService nService;

	@Autowired
	NmsAssetService assetService;

	@RequestMapping(value = "/alarms")
	public List<AlarmBean> getAlarmList() {
		return nmsXService.getAlarms();
	}

	@RequestMapping(value = "/getShowMessage", method = RequestMethod.POST)
	@ResponseBody
	public AssetMessageBean getShowNodeMessage(HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Cache-Control", "no-cache");
		String assetId = request.getParameter("assetId");
		AssetMessageBean assetMessageBean = new AssetMessageBean();
		if (StringUtils.isEmpty(assetId)) {
			return null;
		}
		NmsAsset asset = assetService.findById(Integer.parseInt(assetId));
		assetMessageBean.setAssetId(Integer.parseInt(assetId));
		assetMessageBean.setAssetPos(asset.getAPos());
		/* 判断是否有运行状态告警 */
		List<NmsAlarm> alarmlist = nmsXService.listAlarmByAsset(
				Integer.parseInt(assetId), 0);
		if (alarmlist.size() == 0) {
			assetMessageBean.setStateAlarm("正常");
		} else {
			int alarm_level = 0;
			if (alarmlist != null && alarmlist.size() > 0) {
				for (int i = 0; i < alarmlist.size(); i++) {
					NmsAlarm eventList = (NmsAlarm) alarmlist.get(i);
					if (eventList.getALevel() > alarm_level) {
						alarm_level = eventList.getALevel();
					}
				}
			}
			if (alarm_level == 1) {
				assetMessageBean.setStateAlarm("一般告警");
			} else if (alarm_level == 2) {
				assetMessageBean.setStateAlarm("紧急告警");
			} else if (alarm_level == 3) {
				assetMessageBean.setStateAlarm("严重告警");
			} else {
				assetMessageBean.setStateAlarm("");
			}
		}
		/* 判断是否有违规告警 */
		String weiguiResult = nmsXService.findWGalarmById(Integer
				.parseInt(assetId));
		assetMessageBean.setWeiguiStatus(weiguiResult);

		String pingResult = nmsXService.findPingById(Integer.parseInt(assetId));
		assetMessageBean.setPingStatus(pingResult);

		String cpuByAssetId = nmsXService.findCpuByAssetId(assetId);
		assetMessageBean.setCpuStatus(cpuByAssetId);
		/* Get The RealTime Mem Status nms_mem_info */
		String meninfoById = nmsXService.findMeninfoById(assetId);
		assetMessageBean.setMenStatus(meninfoById);

		assetMessageBean.setAssetIp(asset.getAIp());
		return assetMessageBean;
	}
}
