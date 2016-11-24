const feedItem = (state = {}, action) => {
  switch (action.type) {
    case 'ADD_ITEM':
      return {
        id: action.id,
        text: action.text,
        lat: action.lat,
        long: action.long
      }

    default:
      return state
  }
}

const feed = (state = [], action) => {
  switch (action.type) {
    case 'ADD_ITEM':
      return [
        ...state,
        feedItem(undefined, action)
      ]
    default:
      return state
  }
}

export default feed
