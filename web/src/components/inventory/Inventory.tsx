import InventoryRow from './InventoryRow';

function Inventory() {
  return (
    <div class="flex flex-col w-3/5">
      <InventoryRow numSlots={9}/>
      <InventoryRow numSlots={9}/>
    </div>
  );
}

export default Inventory;
