import { useState } from "react";
import { addFavorite, isFavorite, removeFavorite } from "../lib/favorites";

export default function FavoriteButton({ region, industry }) {
	const [saved, setSaved] = useState(() => isFavorite(region, industry));

	function toggle() {
		if (saved) {
			removeFavorite(region, industry);
			setSaved(false);
		} else {
			addFavorite(region, industry);
			setSaved(true);
		}
	}

	return (
		<button type="button" className={`favorite-button${saved ? " active" : ""}`} onClick={toggle}>
			{saved ? "★ 관심 지역에 저장됨" : "☆ 관심 지역에 저장"}
		</button>
	);
}
