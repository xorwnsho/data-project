import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { extractErrorMessage, getExplore, getIndustries, getRegions } from "../api/queryApi";
import StatsSummary from "../components/StatsSummary";
import StoreMap from "../components/StoreMap";
import FavoriteButton from "../components/FavoriteButton";

export default function ExplorePage() {
	const [regions, setRegions] = useState([]);
	const [industries, setIndustries] = useState([]);
	const [region, setRegion] = useState("");
	const [industry, setIndustry] = useState("");
	const [radius, setRadius] = useState(500);
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);
	const [stats, setStats] = useState(null);

	useEffect(() => {
		getRegions().then(setRegions).catch(() => {});
		getIndustries().then(setIndustries).catch(() => {});
	}, []);

	async function handleSubmit(e) {
		e.preventDefault();
		if (!region.trim() || loading) return;
		setLoading(true);
		setError(null);
		try {
			const data = await getExplore(region.trim(), industry, radius);
			setStats(data);
		} catch (err) {
			setError(extractErrorMessage(err));
			setStats(null);
		} finally {
			setLoading(false);
		}
	}

	return (
		<div className="page">
			<div className="page-intro">
				<h1>상권 지도 탐색기</h1>
				<p>지역과 업종을 골라 반경 내 상가 데이터를 지도와 통계로 바로 확인하세요.</p>
			</div>

			<form className="explore-form" onSubmit={handleSubmit}>
				<div className="explore-field">
					<label htmlFor="region-input">지역</label>
					<input
						id="region-input"
						list="region-options"
						placeholder="예: 둔산동"
						value={region}
						onChange={(e) => setRegion(e.target.value)}
					/>
					<datalist id="region-options">
						{regions.map((r) => (
							<option value={r} key={r} />
						))}
					</datalist>
				</div>

				<div className="explore-field">
					<label htmlFor="industry-select">업종 (선택)</label>
					<select id="industry-select" value={industry} onChange={(e) => setIndustry(e.target.value)}>
						<option value="">전체</option>
						{industries.map((label) => (
							<option value={label} key={label}>{label}</option>
						))}
					</select>
				</div>

				<div className="explore-field">
					<label htmlFor="radius-select">반경</label>
					<select id="radius-select" value={radius} onChange={(e) => setRadius(Number(e.target.value))}>
						<option value={300}>300m</option>
						<option value={500}>500m</option>
						<option value={1000}>1000m</option>
					</select>
				</div>

				<button className="submit-button" type="submit" disabled={loading || !region.trim()}>
					{loading ? "조회 중..." : "탐색하기"}
				</button>
			</form>

			{error && <div className="error-box">{error}</div>}

			{stats && (
				<div className="result-grid">
					<section className="report-panel">
						<StatsSummary stats={stats} />
						<div className="explore-actions">
							<FavoriteButton region={stats.regionName} industry={stats.industryKeyword} />
							<Link
								className="ai-link"
								to="/"
								state={{ prefill: `${stats.regionName}에서 ${stats.industryKeyword ?? ""} 어때?` }}
							>
								AI 상담사에게 물어보기 →
							</Link>
						</div>
					</section>
					<StoreMap stats={stats} />
				</div>
			)}
		</div>
	);
}
