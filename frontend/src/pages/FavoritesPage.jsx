import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getExplore } from "../api/queryApi";
import { getFavorites, removeFavorite } from "../lib/favorites";
import QuickLinks from "../components/QuickLinks";

export default function FavoritesPage() {
	const [favorites, setFavorites] = useState(() => getFavorites());
	const [statsByKey, setStatsByKey] = useState({});
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		let cancelled = false;
		setLoading(true);
		Promise.all(
			favorites.map(async (f) => {
				try {
					const data = await getExplore(f.region, f.industry, 500);
					return [key(f), data];
				} catch {
					return [key(f), null];
				}
			})
		).then((entries) => {
			if (!cancelled) {
				setStatsByKey(Object.fromEntries(entries));
				setLoading(false);
			}
		});
		return () => {
			cancelled = true;
		};
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	function key(f) {
		return `${f.region}::${f.industry || ""}`;
	}

	function handleRemove(f) {
		const next = removeFavorite(f.region, f.industry);
		setFavorites(next);
	}

	if (favorites.length === 0) {
		return (
			<div className="page">
				<div className="page-intro">
					<span className="page-kicker">저장한 지역</span>
					<h1>관심 지역</h1>
				</div>
				<QuickLinks />
				<div className="empty-state">
					<span style={{ fontSize: "2rem" }}>☆</span>
					<p style={{ margin: 0, color: "var(--text-primary)", fontWeight: 600 }}>저장된 관심 지역이 없습니다</p>
					<p style={{ margin: 0 }}>AI 상담 또는 상권 탐색기에서 분석 결과의 ☆ 버튼을 눌러 관심 지역을 저장하세요</p>
					<Link className="ai-link" to="/explore">상권 탐색기로 이동 →</Link>
				</div>
			</div>
		);
	}

	return (
		<div className="page">
			<div className="page-intro">
				<span className="page-kicker">저장한 지역</span>
				<h1>관심 지역</h1>
				<p>저장해 둔 지역의 최신 상권 데이터를 한눈에 확인하세요.</p>
			</div>

			<QuickLinks />

			<div className="favorites-grid">
				{favorites.map((f) => {
					const stats = statsByKey[key(f)];
					return (
						<div className="favorite-card" key={key(f)}>
							<div className="favorite-card-header">
								<div>
									<strong>{f.region}</strong>
									{f.industry && <span className="favorite-industry">{f.industry}</span>}
								</div>
								<button type="button" className="remove-button" onClick={() => handleRemove(f)}>삭제</button>
							</div>

							{loading && !stats && <p className="favorite-loading">불러오는 중...</p>}
							{stats && (
								<div className="favorite-stats">
									<span>전체 상가 {stats.totalStoreCount.toLocaleString()}개</span>
									{f.industry && <span>{f.industry} {stats.competitorCount.toLocaleString()}개</span>}
								</div>
							)}
							<Link
								className="ai-link"
								to="/"
								state={{ prefill: `${f.region}에서 ${f.industry || ""} 어때?` }}
							>
								AI 상담사에게 물어보기 →
							</Link>
						</div>
					);
				})}
			</div>
		</div>
	);
}
