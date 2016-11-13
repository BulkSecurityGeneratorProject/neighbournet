/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import {
  GraphQLObjectType as ObjectType,
  GraphQLID as ID,
  GraphQLString as StringType,
  GraphQLNonNull as NonNull
} from "graphql";

const FeedItemType = new ObjectType({
  name: 'FeedItem',
  fields: () => ({
    id: {
      type: new NonNull(ID),
      resolve: (feed) => feed.id
    },
    text: {
      type: StringType,
      resolve: (feed) => feed.text
    },
  }),
});

export default FeedItemType;
