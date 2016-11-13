/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import FeedItemType from "../types/FeedItemType";
import es6Promise from "es6-promise";
import "isomorphic-fetch";
import {GraphQLList} from "graphql";

es6Promise.polyfill();

const BASE_URL = 'http://localhost:8080/api';

const feed = {
  type: new GraphQLList(FeedItemType),
  resolve: (parent, args, ast) =>
    fetch(`${BASE_URL}/authenticate`, {
      method: 'POST',
      body: JSON.stringify({
        password: 'user',
        username: 'user'
      }),
      redirect: 'follow',
      headers: new Headers({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      })
    })
      .then(json => json.json())
      .then(json =>
        fetch(`${BASE_URL}/feed-items`, {
          headers: new Headers({
            'Authorization': 'Bearer ' + json.id_token,
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          })
        }))
      .then(json => json.json())
}

export default feed;
