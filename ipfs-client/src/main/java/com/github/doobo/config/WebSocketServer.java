package com.github.doobo.config;

import com.alibaba.fastjson.JSON;
import com.github.doobo.model.IpfsSubVO;
import com.github.doobo.soft.SystemClock;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
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

	// concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
	private static final CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<>();


	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		SessionSet.add(session);
		int cnt = OnlineCount.incrementAndGet(); // 在线数加1
		log.info("有连接加入，当前连接数为：{}", cnt);
		IpfsSubVO vo = new IpfsSubVO();
		vo.setId(0L);
		vo.setTime(SystemClock.now());
		vo.setFromSessionId(session.getId());
		sendMessage(session, JSON.toJSONString(ResultUtils.of(vo)));
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session) {
		SessionSet.remove(session);
		int cnt = OnlineCount.decrementAndGet();
		log.info("有连接关闭，当前连接数为：{}", cnt);
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
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
		error.printStackTrace();
	}

	/**
	 * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
	 * @param session
	 * @param message
	 */
	public static void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			log.error("发送消息出错", e);
		}
	}

	/**
	 * 群发消息
	 */
	public static void broadCastInfo(String message) {
		for (Session session : SessionSet) {
			if(session.isOpen()){
				sendMessage(session, message);
			}
		}
	}

	/**
	 * 指定Session发送消息
	 */
	public static boolean sendMessage(String message, String sessionId) {
		Session session = null;
		for (Session s : SessionSet) {
			if(s.getId().equals(sessionId)){
				session = s;
				break;
			}
		}
		if(session!=null){
			sendMessage(session, message);
		} else{
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
