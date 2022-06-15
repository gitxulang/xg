package iie.domain;

import java.io.Serializable;
import java.util.List;


public class RoomPageConfigJson implements Serializable {
    public int roomid;

    public List<Webjson> webjson;

    public String resource;

    public RoomPageConfigJson() {
    }

    public RoomPageConfigJson(int roomid, List<Webjson> webjson, String resource) {
        this.roomid = roomid;
        this.webjson = webjson;
        this.resource = resource;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int getRoomid() {
        return this.roomid;
    }

    public void setWebjson(List<Webjson> webjson) {
        this.webjson = webjson;
    }

    public List<Webjson> getWebjson() {
        return this.webjson;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;

    }

    public class Webjson implements Serializable
    {
        public Webjson() {
        }

        public Webjson(List<Integer> pos, List<Integer> tpos, String name) {
            this.pos = pos;
            this.tpos = tpos;
            this.name = name;
        }

        public List<Integer> pos;

        public  List<Integer> tpos;

        public String name;

        public void setPos(List<Integer> pos){
            this.pos = pos;
        }
        public List<Integer> getPos(){
            return this.pos;
        }
        public void setTpos(List<Integer> tpos){
            this.tpos = tpos;
        }
        public List<Integer> getTpos(){
            return this.tpos;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
    }
}