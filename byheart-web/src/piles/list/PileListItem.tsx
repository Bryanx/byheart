import Pile from '../models/Pile'
import ColorUtil from '../../shared/util/ColorUtil';
import './PileListItem.scss';

const PileListItem = (props: { pile?: Pile }) => (
    <div className="stack bg-gray-100 dark:bg-gray-800 rounded-2xl flex justify-center items-center shadow">
      <div className="text-center p-1">
        <p className="pile-name text-gray-500 dark:text-white text-2xl"
           style={{color: ColorUtil.argbToRGB(props.pile?.color)}}>
          {props.pile?.name}
        </p>
        <p className="text-gray-500 dark:text-white text-xs">
          {props.pile?.cards?.length ?? 0} CARDS
        </p>
      </div>
    </div>
);

export default PileListItem

