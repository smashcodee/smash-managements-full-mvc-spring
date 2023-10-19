package br.com.smashcode.smashmanagements.globalchat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class WebSocketController {
    // ajuda a redirecionar as mensagens para o usuário correto;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // "lista" de usuários conectados.
    private final Set<String> connectedUsers;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
        connectedUsers = new HashSet<>();
    }

    @MessageMapping("/register")
    @SendToUser("/queue/newMember")
    public Set<String> registerUser(String webChatUsername){
        if(!connectedUsers.contains(webChatUsername)) {
            connectedUsers.add(webChatUsername);
            simpMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername);
            return connectedUsers;
        } else {
            return new HashSet<>();
        }
    }

    @MessageMapping("/connect")
    @SendTo("/topic/connectedUser")
    public String registerOrConnectUser(String webChatUsername) {
        if (!connectedUsers.contains(webChatUsername)) {
            connectedUsers.add(webChatUsername);
            simpMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername);
            return webChatUsername + " está conectado e pode usar o chat global.";
        } else {
            return webChatUsername + " já está registrado ou conectado.";
        }
    }

    @MessageMapping("/unregister")
    @SendTo("/topic/disconnectedUser")
    public String disconnectUser(String webChatUsername){
        connectedUsers.remove(webChatUsername);
        // internacionalizar depois...
        return webChatUsername + " não está mais conectado ao chat global.";
    }

    @MessageMapping("/message")  //6
    public void greeting(WebSocketMessage message){
        simpMessagingTemplate.convertAndSendToUser(message.toWhom, "/msg", message);
    }
}
