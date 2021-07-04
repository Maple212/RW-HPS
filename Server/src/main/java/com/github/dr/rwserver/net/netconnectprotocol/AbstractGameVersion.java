package com.github.dr.rwserver.net.netconnectprotocol;

import com.github.dr.rwserver.data.Player;
import com.github.dr.rwserver.data.global.Data;
import com.github.dr.rwserver.data.global.NetStaticData;
import com.github.dr.rwserver.io.GameInputStream;
import com.github.dr.rwserver.io.GameOutputStream;
import com.github.dr.rwserver.io.Packet;
import com.github.dr.rwserver.net.ConnectionAgreement;
import com.github.dr.rwserver.net.GroupNet;
import com.github.dr.rwserver.net.core.AbstractNetConnect;
import com.github.dr.rwserver.util.Time;
import com.github.dr.rwserver.util.log.Log;
import com.github.dr.rwserver.util.zip.gzip.GzipEncoder;
import okhttp3.internal.cache2.Relay;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Dr
 */
public abstract class AbstractGameVersion implements AbstractNetConnect {
    protected int errorTry = 0;
    protected boolean isPasswd = false;
    protected long lastReceivedTime = Time.concurrentMillis();
    protected volatile boolean isDis = false;
    protected volatile boolean isTry = false;
    protected Player player = null;

    protected ConnectionAgreement connectionAgreement;

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getIp() {
        return connectionAgreement.ip;
    }

    @Override
    public int getPort() {
        return connectionAgreement.localPort;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setCache(Packet packet) {
    }

    @Override
    public void setTry() {
        errorTry++;
    }

    @Override
    public int getTry() {
        return errorTry;
    }

    @Override
    public void setTryBoolean(boolean tryBoolean) {
        this.isTry = tryBoolean;
    }

    @Override
    public boolean getTryBoolean() {
        return isTry;
    }

    @Override
    public boolean getIsPasswd() {
        return isPasswd;
    }

    @Override
    public void setLastReceivedTime() {
        this.isTry = false;
        this.lastReceivedTime = Time.concurrentMillis();
    }

    @Override
    public long getLastReceivedTime() {
        return lastReceivedTime;
    }

    @Override
    public Relay getRelay() {
        return null;
    }

    @Override
    public void setConnectionAgreement(ConnectionAgreement connectionAgreement) {
        this.connectionAgreement = connectionAgreement;
    }

    @Override
    public String getConnectionAgreement() {
        return connectionAgreement.useAgreement;
    }

    @Override
    public String getVersion() {
        return "1.14";
    }

    @Override
    public void sendSystemMessage(String msg) {
        try {
            sendPacket(NetStaticData.protocolData.abstractNetPacket.getSystemMessageByteBuf(msg));
        } catch (IOException e) {
            Log.error("[Player] Send System Chat Error",e);
        }
    }

    @Override
    public void sendChatMessage(String msg, String sendBy, int team) {
        try {
            sendPacket(NetStaticData.protocolData.abstractNetPacket.getChatMessageByteBuf(msg,sendBy,team));
        } catch (IOException e) {
            Log.error("[Player] Send Player Chat Error",e);
        }
    }

    @Override
    public void sendServerInfo(boolean utilData) throws IOException {
    }

    @Override
    public void sendSurrender() {
    }

    @Override
    public void sendKick(String reason) throws IOException {
    }

    @Override
    public void ping() {
        try {
            sendPacket(NetStaticData.protocolData.abstractNetPacket.getPingByteBuf(player));
        } catch (IOException e) {
            errorTry++;
        }
    }

    @Override
    public byte[] getGameSaveData(Packet packet) throws IOException {
        return null;
    }

    @Override
    public void receiveChat(Packet p) throws IOException {
    }

    @Override
    public void receiveCommand(Packet p) throws IOException {
    }

    @Override
    public void sendStartGame() throws IOException {
    }

    @Override
    public void sendTeamData(GzipEncoder gzip) {
    }

    @Override
    public boolean getPlayerInfo(Packet p) throws IOException {
        return false;
    }

    @Override
    public void registerConnection(Packet p) throws IOException {
    }

    @Override
    public void sendErrorPasswd() throws IOException {
    }

    protected void close(final GroupNet groupNet) {
        try {
            connectionAgreement.close(groupNet);
        } catch (Exception e) {
            Log.error("Close Connect",e);
        }
    }

    @Override
    public void debug(Packet packet) {
        try (GameInputStream stream = new GameInputStream(packet)) {
            Data.LOGCOMMAND.handleMessage(URLDecoder.decode(stream.readString(), StandardCharsets.UTF_8),this);
        } catch (IOException e) {
        }
    }

    @Override
    public void sendDebug(String str) {
        try {
            GameOutputStream o = new GameOutputStream();
            o.writeString(str);
            sendPacket(o.createPacket(2001));
        } catch (Exception e) {
        }
    }

    @Override
    public void getGameSave() {
    }

    @Override
    public void sendGameSave(Packet packet) {
        sendPacket(packet);
    }

    /**
     * 发送包
     * @param packet 数据
     */
    @Override
    public void sendPacket(Packet packet) {
        try {
            connectionAgreement.send(packet);
        } catch (Exception e) {
            Log.error("[UDP] SendError - 本消息单独出现无妨 连续多次出现请debug",e);
            disconnect();
        }
    }
}