import { CircleMarker, MapContainer, Popup, TileLayer, useMap } from "react-leaflet";
import { useEffect, useMemo } from "react";

const MAX_MARKERS = 500;

function isMatched(store, targets) {
	if (!targets || targets.length === 0) return false;
	return targets.some((target) => store.industry?.includes(target));
}

function FitBounds({ points }) {
	const map = useMap();

	useEffect(() => {
		if (points.length === 0) return;
		const bounds = points.map((p) => [p.lat, p.lon]);
		map.fitBounds(bounds, { padding: [24, 24] });
	}, [points, map]);

	return null;
}

export default function StoreMap({ stats }) {
	const { stores, industryTargets, regionName } = stats;

	const points = useMemo(() => {
		const withCoords = stores.filter((s) => s.lat != null && s.lon != null);
		const matched = withCoords.filter((s) => isMatched(s, industryTargets));
		const rest = withCoords.filter((s) => !isMatched(s, industryTargets));
		// 경쟁업체(matched)를 우선 표시하고, 남는 슬롯을 나머지 상가로 채운다.
		return [...matched, ...rest].slice(0, MAX_MARKERS);
	}, [stores, industryTargets]);

	return (
		<section className="map-panel">
			<div className="map-panel-header">
				<h3>{regionName} 상권 지도 · 반경 500m</h3>
				<div className="map-legend">
					{industryTargets?.length > 0 && (
						<span><i className="legend-dot matched" />경쟁업체</span>
					)}
					<span><i className="legend-dot general" />기타 상가</span>
				</div>
			</div>

			{points.length === 0 ? (
				<div className="map-empty">지도에 표시할 상가 위치가 없습니다.</div>
			) : (
				<div className="map-wrapper">
					<MapContainer
						center={[points[0].lat, points[0].lon]}
						zoom={16}
						scrollWheelZoom={true}
						style={{ height: "100%", width: "100%" }}
					>
						<TileLayer
							attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
							url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
						/>
						<FitBounds points={points} />
						{points.map((store, i) => {
							const matched = isMatched(store, industryTargets);
							return (
								<CircleMarker
									key={i}
									center={[store.lat, store.lon]}
									radius={matched ? 6 : 4}
									pathOptions={{
										color: matched ? "#d9480f" : "#1c5cab",
										fillColor: matched ? "#eb6834" : "#2a78d6",
										fillOpacity: matched ? 0.9 : 0.6,
										weight: 1,
									}}
								>
									<Popup>
										<strong>{store.name}</strong>
										<br />
										{store.industry}
										<br />
										{store.address}
									</Popup>
								</CircleMarker>
							);
						})}
					</MapContainer>
					{stores.length > MAX_MARKERS && (
						<p className="map-note">전체 {stores.length}개 중 {MAX_MARKERS}개만 지도에 표시됩니다.</p>
					)}
				</div>
			)}
		</section>
	);
}
