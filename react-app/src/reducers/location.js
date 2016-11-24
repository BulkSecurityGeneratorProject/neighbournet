/**
 * Created by sander on 5/11/2016.
 */

const location = (state = {}, action) => {
  switch (action.type) {
    case 'SET_LOCATION':
      return {
        lat: action.lat,
        long: action.long
      };
    default:
      return state;
  }
};

export default location;
