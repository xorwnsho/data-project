import { CircleMarker, MapContainer, Popup, TileLayer, useMap } from "react-leaflet";
import { useEffect, useMemo } from "react";

const MAX_MARKERS = 500;

function FitBounds({ points }) {
	const map = useMap();

	useEffect(() => {
		if (points.length === 0) return;
		const bounds = points.map((p) => [p.lat, p.lon]);
		map.fitBounds(bounds, { padding: [24, 24] });
	}, [points, map]);

	return null;
}

export default function StoreMap({ stores }) {
	const points = useMemo(() => {
		return stores
			.filter((s) => s.lat != null && s.lon != null)
			.slice(0, MAX_MARKERS);
	}, [stores]);

	if (points.length === 0) {
		return <div className="map-empty">지도에 표시할 상가 위치가 없습니다.</div>;
	}

	const center = [points[0].lat, points[0].lon];

	return (
		<div className="map-wrapper">
			<MapContainer center={center} zoom={16} scrollWheelZoom={true} style={{ height: "100%", width: "100%" }}>
				<TileLayer
					attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
					url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
				/>
				<FitBounds points={points} />
				{points.map((store, i) => (
					<CircleMarker
						key={i}
						center={[store.lat, store.lon]}
						radius={5}
						pathOptions={{ color: "#d9480f", fillColor: "#f76707", fillOpacity: 0.8 }}
					>
						<Popup>
							<strong>{store.name}</strong>
							<br />
							{store.industry}
							<br />
							{store.address}
						</Popup>
					</CircleMarker>
				))}
			</MapContainer>
			{stores.length > MAX_MARKERS && (
				<p className="map-note">전체 {stores.length}개 중 {MAX_MARKERS}개만 지도에 표시됩니다.</p>
			)}
		</div>
	);
}
