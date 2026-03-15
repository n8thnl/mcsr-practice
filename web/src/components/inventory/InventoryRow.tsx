import InventorySlot from './InventorySlot';

function InventoryRow(props) {
  return (
    <div class="flex flex-row justify-around">
    {
      Array(props.numSlots)
        .fill()
        .map((_, i) => <InventorySlot />)
    }
    </div>
  )
}

export default InventoryRow;
