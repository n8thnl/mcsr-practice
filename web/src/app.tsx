import { useState } from 'preact/hooks';
import Inventory from './components/inventory/Inventory';
import ItemSearch from './components/item/search/ItemSearch';

export function App() {
  const [count, setCount] = useState(0)

  return (
    <div class="flex flex-col items-center justify-center mt-30 h-full">
      <ItemSearch />
      <div class="my-8" />
      <Inventory />
    </div>
  )
}
