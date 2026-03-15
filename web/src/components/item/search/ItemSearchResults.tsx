import ItemFrame from '../../item/ItemFrame';

function ItemSearchResults(props) {
  return (
    <div id="item-search-results" class="grid grid-flow-row grid-cols-9 overflow-y-scroll">
      {
        (props.items || []).map(item => <ItemFrame itemId={item.id} />)
      }
    </div>
  );
}

export default ItemSearchResults;
