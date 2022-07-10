package com.github.doobo.scan;

import com.alibaba.fastjson.JSON;
import com.github.doobo.jms.ExchangeMsg;
import com.github.doobo.soft.SequenceUtils;
import com.github.doobo.soft.SystemClock;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint(value = "/ipfs/sub")
public class WebSocketServer {

	@PostConstruct
	public void init() {
		log.debug("webSocket init.");
	}

	private static final AtomicInteger OnlineCount = new AtomicInteger(0);

	//concurrent包的线程安全Map，用来存放每个客户端对应的Session对象
	private static final ConcurrentHashMap<Long, Session> SESSION_MAP = new ConcurrentHashMap<>();

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		int cnt = OnlineCount.incrementAndGet(); // 在线数加1
		log.info("有连接加入，当前连接数为：{}", cnt);
		long id = SequenceUtils.nextId();
		ExchangeMsg vo = new ExchangeMsg();
		vo.setMsgId(id);
		vo.setTimeStamp(SystemClock.now());
		vo.setSessionId(session.getId());
		vo.setRequestId(id);
		SESSION_MAP.put(id, session);
		sendMessage(session, JSON.toJSONString(ResultUtils.of(vo)));
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session) {
		if(!SESSION_MAP.containsValue(session)){
			return;
		}
		for(Long item : SESSION_MAP.keySet()){
			if(session.equals(SESSION_MAP.get(item))){
				SESSION_MAP.remove(item);
				int cnt = OnlineCount.decrementAndGet();
				log.info("有连接关闭，当前连接数为：{}", cnt);
				break;
			}
		}
	}

	/**
	 * 适用于分布式环境下的Session关闭
	 */
	public void removeSessionById(Long id){
		if(SESSION_MAP.containsKey(id)){
			SESSION_MAP.remove(id);
			int cnt = OnlineCount.decrementAndGet();
			log.info("有连接关闭，当前连接数为：{}", cnt);
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		 log.info("来自客户端的消息：{}",message);
		 //暂不处理发送过来的消息,后期可处理用户登录与当前session绑定
		//SendMessage(session, "收到消息"+message);
	}

	/**
	 * 出现错误
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
	}

	/**
	 * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
	 */
	public static void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (Exception e) {
			log.error("发送消息出错", e);
		}
	}

	/**
	 * 群发消息
	 */
	public static void broadCastInfo(String message) {
		for (Session session : SESSION_MAP.values()) {
			if(session.isOpen()){
				sendMessage(session, message);
			}
		}
	}

	/**
	 * 指定Session发送消息,分布式环境下必须适用返回的指定ID
	 */
	public static boolean sendMessage(String message, Long sessionId) {
		if(!SESSION_MAP.containsKey(sessionId)){
			return Boolean.FALSE;
		}
		Session session = SESSION_MAP.get(sessionId);
		sendMessage(session, message);
		return Boolean.TRUE;
	}
}
