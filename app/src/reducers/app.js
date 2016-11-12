/**
 * Created by sander on 5/11/2016.
 */

import {combineReducers} from "redux";
import location from "./location";
import feed from "./feed";

const app = combineReducers({
  location,
  feed
})

export default app
