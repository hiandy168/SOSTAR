package com.hyphenate.easeui.commonutils;

import android.content.Context;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by renyu on 2017/1/6.
 */

public class Utils {

    /**
     * 初始化 SDK
     * @param context
     */
    public static void initChatOptions(final Context context) {
        // 获取到EMChatOptions对象
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置是否需要已读回执
        options.setRequireAck(true);
        // 设置是否需要已送达回执
        options.setRequireDeliveryAck(false);
        options.setAutoAcceptGroupInvitation(false);
        EMClient.getInstance().init(context, options);
        // 设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
        EMClient.getInstance().setDebugMode(true);
        // 注册连接状态监听
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int error) {
                if(error == EMError.USER_REMOVED){
                    // 显示帐号已经被移除
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                } else {
                    if (NetUtils.hasNetwork(context)) {
                        //连接不到聊天服务器
                    }
                    else {
                        //当前网络不可用，请检查网络设置
                    }

                }
            }
        });
        //接收消息
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //收到消息
                Log.d("Utils", "list:" + list);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                //消息状态变动
            }
        });
        //监听好友状态事件
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                //好友请求被同意
            }

            @Override
            public void onContactDeleted(String s) {
                //好友请求被拒绝
            }

            @Override
            public void onContactInvited(String s, String s1) {
                //收到好友邀请
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //增加了联系人时回调此方法
            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //被删除时回调此方法
            }
        });
        EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
            @Override
            public void onInvitationReceived(String s, String s1, String s2, String s3) {
                //收到加入群组的邀请
            }

            @Override
            public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {
                //收到加群申请
            }

            @Override
            public void onRequestToJoinAccepted(String s, String s1, String s2) {
                //加群申请被同意
            }

            @Override
            public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {
                // 加群申请被拒绝
            }

            @Override
            public void onInvitationAccepted(String s, String s1, String s2) {
                //群组邀请被接受
            }

            @Override
            public void onInvitationDeclined(String s, String s1, String s2) {
                //群组邀请被拒绝
            }

            @Override
            public void onUserRemoved(String s, String s1) {
                //当前用户被管理员移除出群组
            }

            @Override
            public void onGroupDestroyed(String s, String s1) {
                //群组被创建者解散
            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

            }
        });
    }

    /**
     * 登录
     * @param currentUsername
     * @param currentPassword
     */
    public static void login(String currentUsername, String currentPassword) {
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                loadAllGroups();
                loadAllConversations();
                getAllContactsFromServer();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {

            }
        });
    }

    /**
     * 更新当前用户的在苹果 APNS 推送的昵称
     * @param nickname
     */
    public static void updateCurrentUserNick(String nickname) {
        EMClient.getInstance().updateCurrentUserNick(nickname);
    }

    /**
     * 加载所有本地群
     */
    public static void loadAllGroups() {
        EMClient.getInstance().groupManager().loadAllGroups();
    }

    /**
     * 加载所有回话
     */
    public static void loadAllConversations() {
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    /**
     * 发送文本消息
     * @param content 消息文字内容
     * @param toChatUsername 对方用户或者群聊的id
     */
    public static void sendMessage(String content, String toChatUsername) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
//        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送语音消息
     * @param filePath 语音文件路径
     * @param length 录音时间(秒)
     * @param toChatUsername 对方用户或者群聊的id
     */
    public static void sendVoice(String filePath, int length, String toChatUsername) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
//        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送视频消息
     * @param videoPath 视频本地路径
     * @param thumbPath 视频预览图路径
     * @param videoLength 视频时间长度
     * @param toChatUsername 对方用户或者群聊的id
     */
    public static void sendVideo(String videoPath, String thumbPath, int videoLength, String toChatUsername) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
//        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送地理位置消息
     * @param latitude 纬度
     * @param longitude 经度
     * @param locationAddress 具体位置内容
     * @param toChatUsername 对方用户或者群聊的id
     */
    public static void sendLocation(double latitude, double longitude, String locationAddress, String toChatUsername) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
//        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送文件消息
     * @param filePath 文件本地路径
     * @param toChatUsername 对方用户或者群聊的id
     */
    public static void sendFile(String filePath, String toChatUsername) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
//        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 发送透传消息
     * @param action
     * @param toChatUsername 对方用户或者群聊的id
     */
    public static void sendCmd(String action, String toChatUsername) {
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.CMD);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
//        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        message.setTo(toChatUsername);
        message.addBody(cmdBody);
        //设置扩展属性
//        message.setAttribute();
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 获取未读消息数量
     * @param username
     */
    public static void getUnreadMsgCount(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        conversation.getUnreadMsgCount();

    }

    /**
     * 指定会话消息未读数清零
     * @param username
     */
    public static void markAllMessagesAsRead(String username) {
        EMClient.getInstance().chatManager().getConversation(username).markAllMessagesAsRead();
    }

    /**
     * 把一条消息置为已读
     * @param username
     */
    public static void markMessageAsRead(String username, String messageId) {
        EMClient.getInstance().chatManager().getConversation(username).markMessageAsRead(messageId);
    }

    /**
     * 所有未读消息数清零
     * @param username
     */
    public static void markAllConversationsAsRead(String username) {
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    /**
     * 获取此会话在本地的所有的消息数量
     * @param username
     */
    public static int getAllMessages(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        return conversation.getAllMsgCount();
    }

    /**
     * 获取所有会话
     * @return
     */
    public static Map<String, EMConversation> getAllConversations() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        return conversations;
    }

    /**
     * 删除和某个user会话
     * @param username
     * @param keep 如果需要保留聊天记录，传false
     */
    public static void deleteConversation(String username, boolean keep) {
        EMClient.getInstance().chatManager().deleteConversation(username, keep);
    }

    /**
     * 删除聊天记录
     * @param username
     * @param msgId
     */
    public static void removeMessage(String username, String msgId) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        conversation.removeMessage(msgId);
    }

    /**
     * 导入消息到数据库
     * @param msgs
     */
    public static void importMessages(List<EMMessage> msgs) {
        EMClient.getInstance().chatManager().importMessages(msgs);
    }

    /**
     * 获取好友列表
     * @return
     */
    public static List<String> getAllContactsFromServer() {
        try {
            return EMClient.getInstance().contactManager().getAllContactsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加好友
     * @param toAddUsername
     * @param reason
     */
    public static void addContact(String toAddUsername, String reason) {
        try {
            EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除好友
     * @param username
     */
    public static void deleteContact(String username) {
        try {
            EMClient.getInstance().contactManager().deleteContact(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同意好友请求
     * @param username
     */
    public static void acceptInvitation(String username) {
        try {
            EMClient.getInstance().contactManager().acceptInvitation(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拒绝好友请求
     * @param username
     */
    public static void declineInvitation(String username) {
        try {
            EMClient.getInstance().contactManager().declineInvitation(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param maxUsers 群组类型选项，可以设置群组最大用户数(默认200)及群组类型{@link EMGroupManager.EMGroupStyle}
     *                 EMGroupStylePrivateOnlyOwnerInvite ——私有群，只有群主可以邀请人；
                       EMGroupStylePrivateMemberCanInvite ——私有群，群成员也能邀请人进群；
                       EMGroupStylePublicJoinNeedApproval ——公开群，加入此群除了群主邀请，只能通过申请加入此群；
                       EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。
     * @param groupName 群组名称
     * @param desc 群组简介
     * @param allMembers 群组初始成员，如果只有自己传空数组即可
     * @param reason 邀请成员加入的reason
     */
    public static void createGroup(int maxUsers, EMGroupManager.EMGroupStyle style, String groupName, String desc, String[] allMembers, String reason) {
        EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
        option.maxUsers = maxUsers;
        option.style = style;
        try {
            EMClient.getInstance().groupManager().createGroup(groupName, desc, allMembers, reason, option);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群主加人
     * @param groupId
     * @param newmembers
     */
    public static void addUsersToGroup(String groupId, String[] newmembers) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私有群里，如果开放了群成员邀请
     * @param groupId
     * @param newmembers
     */
    public static void inviteUser(String groupId, String[] newmembers) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群组踢人
     * @param groupId
     * @param username
     */
    public static void removeUserFromGroup(String groupId, String username) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().removeUserFromGroup(groupId, username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入某个群组
     * 如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
     * @param groupid
     */
    public static void joinGroup(String groupid) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().joinGroup(groupid);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入某个群组
     * 需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
     * @param groupid
     * @param reason
     */
    public static void applyJoinToGroup(String groupid, String reason) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().applyJoinToGroup(groupid, reason);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出群组
     * @param groupid
     */
    public static void leaveGroup(String groupid) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().leaveGroup(groupid);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解散群组
     * @param groupid
     */
    public static void destroyGroup(String groupid) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().destroyGroup(groupid);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器获取自己加入的和创建的群组列表
     * 此api获取的群组sdk会自动保存到内存和db。
     * @return
     */
    public static List<EMGroup> getJoinedGroupsFromServer() {
        try {
            //需异步处理
            return EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从本地加载群组列表
     */
    public static List<EMGroup> getAllGroups() {
        return EMClient.getInstance().groupManager().getAllGroups();
    }

    /**
     * 修改群组名称
     * @param groupId 需要改变名称的群组的id
     * @param changedGroupName 改变后的群组名称
     */
    public static void changeGroupName(String groupId, String changedGroupName) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().changeGroupName(groupId, changedGroupName);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地获取群组基本信息
     * @param groupId
     * @return
     */
    public static EMGroup getGroup(String groupId) {
        return EMClient.getInstance().groupManager().getGroup(groupId);
    }

    /**
     * 从服务器获取群组基本信息
     * @param groupId
     * @return
     */
    public static EMGroup getGroupFromServer(String groupId) {
        try {
            return EMClient.getInstance().groupManager().getGroupFromServer(groupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 屏蔽群消息
     * @param groupId
     */
    public static void blockGroupMessage(String groupId) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().blockGroupMessage(groupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消屏蔽群消息
     * @param groupId
     */
    public static void unblockGroupMessage(String groupId) {
        try {
            //需异步处理
            EMClient.getInstance().groupManager().unblockGroupMessage(groupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }
}
