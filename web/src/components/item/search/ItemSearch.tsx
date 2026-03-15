import { useState, useEffect } from 'preact/hooks';
import ItemSearchResults from './ItemSearchResults';
import itemIndex from '../../../assets/item-index.json';

function ItemSearch(props) {
  const [searchString, setSearchString] = useState("");
  const [searchResultItems, setSearchResultItems] = useState([]);

  const onSearchStringChange = (newValue: string) => {
    console.log(newValue);
  };

  useEffect(() => {
    const foundItems = itemIndex.filter(item => {
      return searchString !== ''
        && (
          item.id.startsWith(searchString)
          || item.name.startsWith(searchString)
          || item.name.split(' ').filter(word => word.startsWith(searchString)).length > 0
        );
    });

    setSearchResultItems(foundItems);
  }, [searchString]);

  return (
    <div class="w-3/5 min-h-1/5 flex flex-col border-3 rounded-lg h-100">
      <input
        type="text"
        id="item-search"
        placeholder="Search"
        class="w-full pl-2 py-2 border-b-3"
        value={searchString}
        oninput={ e => setSearchString(e.target.value) }
      />
      <ItemSearchResults items={searchResultItems} />
    </div>
  );
}

export default ItemSearch;

