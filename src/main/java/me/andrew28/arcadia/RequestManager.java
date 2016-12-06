package me.andrew28.arcadia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class RequestManager {
    private static HashMap<Request,RequestHandler> pendingRequests = new HashMap<>();

    public static void registerRequest(Request request, RequestHandler requestHandler){
        pendingRequests.put(request, requestHandler);
        request.setWhenReady(() -> {
            requestHandler.ready(request);
            pendingRequests.remove(request);
        });
    }

    public static Boolean memberIsAlreadyHosting(String member){
        for (Request request : pendingRequests.keySet()){
            if (request.getHost().equals(member)){
                return true;
            }
        }
        return false;
    }

    public static Request getRequestMemberIsHosting(String member){
        for (Request request : pendingRequests.keySet()){
            if (request.getHost().equals(member)){
                return request;
            }
        }
        return null;
    }

    public static Boolean memberHasRequest(String member, String host){
        for (Request request : pendingRequests.keySet()){
            if (request.getHost().equals(host)){
                for(String partyMember : request.getParty()){
                    if (partyMember.equals(member)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Request getRequestForMember(String member, String host){
        for (Request request : pendingRequests.keySet()){
            if (request.getHost().equals(host)){
                for(String partyMember : request.getParty()){
                    if (partyMember.equals(member)){
                        return request;
                    }
                }
            }
        }
        return null;
    }

    public interface RequestHandler{
        void ready(Request request);
    }
    public static class Request{
        String[] party;
        String host;
        String gameName;
        ArrayList<String> accepted = new ArrayList<>();
        Runnable whenReady;
        public Request(String host, String gameName, String... party){
            this.party = party;
            this.host = host;
            this.gameName = gameName;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public void setWhenReady(Runnable r){
            whenReady = r;
        }

        public void accept(String member){
            accepted.add(member);
            if (ready()){
                if (whenReady != null){
                    whenReady.run();
                }
            }
        }

        public boolean ready(){
            ArrayList<String> opponents = new ArrayList<>(Arrays.asList(getParty()));
            opponents.remove(getHost());
            for (String member : opponents){
                if (!accepted.contains(member)){
                    return false;
                }
            }
            return true;
        }

        public String[] getNotReady(){
            ArrayList<String> opponents = new ArrayList<>(Arrays.asList(getParty()));
            ArrayList<String> waitingOn = new ArrayList<>();
            opponents.remove(getHost());
            for (String member : opponents){
                if (!accepted.contains(member)){
                    waitingOn.add(member);
                }
            }
            return waitingOn.toArray(new String[waitingOn.size()]);
        }

        public String[] getParty() {
            return party;
        }

        public void setParty(String[] party) {
            this.party = party;
        }
    }
}
