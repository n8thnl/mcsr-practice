function ItemFrame(props) {
  return (
    <div class="border-2 rounded aspect-square grow m-1 content-center justify-items-center bg-gray-200">
      <img src={`/minecraft-items/${props.itemId}.png`} class=""/>
    </div>
  )
}

export default ItemFrame;
