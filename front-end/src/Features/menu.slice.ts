import { createSlice } from "@reduxjs/toolkit";
import { stat } from "fs";

export interface MenuCurrentState {
    open: "product" | "account",
    current: "product-list" | "product-add" | "change-password",
}

export const menuState: MenuCurrentState = {
    open: "product",
    current: "product-list",
};

export const menuSlice = createSlice({
    name: 'menu',
    initialState: localStorage.getItem('token') ? menuState : {} as MenuCurrentState,
    reducers: {
        setCurrent: (state, action) => {
            console.log(action.payload);
            
            state.current = action.payload.current;
            state.open = action.payload.open;
        }
    }
});

export const { setCurrent } = menuSlice.actions;
const menuReducer = menuSlice.reducer;
export default menuReducer;

