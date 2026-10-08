import java.util.ArrayList;

public class KitchenState implements State {
    @Override
    public void enter(Model model, View view) {
        view.Print("You enter the kitchen. It smells of rotted food and old dust. The cabinets hang open, their hinges broken.");
        if (model.cabinetStuck()) {
            view.Print("...All except one, which is stuck tight. If only you had something to pry it open...");
        }
        if (model.storageRoomLocked()) {
            view.Print("The door to the storage room is shut, held tight by a brass lock.");
        } else {
            view.Print("The storage room door hangs open.");
        }
    }

    @Override
    public ArrayList<String> getActions(Model model) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Enter Living Room");

        if (model.hasStorageRoomKey() && model.storageRoomLocked()) {
            actions.add("Unlock Storage Room");
        }
        if (!model.storageRoomLocked()) {
            actions.add("Enter Storage Room");
        }
        if (model.hasCrowbar() && model.cabinetStuck()) {
            actions.add("Pry Open Cabinet with Crowbar");
        }
        if (!model.hasClosetKey() && !model.cabinetStuck()) {
            actions.add("Pick up Steel Key");
        }
        return actions;
    }

    @Override
    public State handleAction(Model model, View view, String action) {
        switch (action) {
            case "Enter Living Room":
                model.decrementTime();
                return model.getStateFromRoom(Room.LIVING_ROOM);
            case "Unlock Storage Room":
                model.unlockStorageRoom();
                view.Print("The storage room door unlocks. Dust falls off of the shivering door.");
                return this;
            case "Enter Storage Room":
                model.decrementTime();
                return model.getStateFromRoom(Room.STORAGE_ROOM);
            case "Pry Open Cabinet with Crowbar":
                model.openCabinet();
                view.Print("CRACK! You force the stuck cabinet open with the crowbar.");
                view.Print("Inside the dusty cabinet is nothing but an old steel key.");
                return this;
            case "Pick up Steel Key":
                model.grabClosetKey();
                view.Print("You pocket the steel key. There must be a matching lock around here somewhere.");
                return this;
            default:
                view.Print("You can't do that.");
                return this;
        }
    }

    @Override
    public Room getRoom() {
        return Room.KITCHEN;
    }
}