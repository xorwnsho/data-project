import { useEffect, useState } from "react";
import { extractErrorMessage, getIndustries, getRegions, postCompare } from "../api/queryApi";
import QuickLinks from "../components/QuickLinks";
import StatsSummary from "../components/StatsSummary";
import ReportText from "../components/ReportText";
import StoreMap from "../components/StoreMap";

function TargetPicker({ label, pill, value, onChange, regions, industries }) {
	return (
		<div className="compare-picker">
			<h3 className={pill}>{label}</h3>
			<div className="explore-field">
				<label>지역</label>
				<input
					list="region-options"
					placeholder="예: 둔산동"
					value={value.region}
					onChange={(e) => onChange({ ...value, region: e.target.value })}
				/>
			</div>
			<div className="explore-field">
				<label>업종 (선택)</label>
				<select value={value.industry} onChange={(e) => onChange({ ...value, industry: e.target.value })}>
					<option value="">전체</option>
					{industries.map((i) => (
						<option value={i} key={i}>{i}</option>
					))}
				</select>
			</div>
			<datalist id="region-options">
				{regions.map((r) => (
					<option value={r} key={r} />
				))}
			</datalist>
		</div>
	);
}

export default function ComparePage() {
	const [regions, setRegions] = useState([]);
	const [industries, setIndustries] = useState([]);
	const [a, setA] = useState({ region: "둔산동", industry: "카페" });
	const [b, setB] = useState({ region: "노은동", industry: "카페" });
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);
	const [result, setResult] = useState(null);

	useEffect(() => {
		getRegions().then(setRegions).catch(() => {});
		getIndustries().then(setIndustries).catch(() => {});
	}, []);

	async function handleSubmit(e) {
		e.preventDefault();
		if (!a.region.trim() || !b.region.trim() || loading) return;
		setLoading(true);
		setError(null);
		try {
			const data = await postCompare(a, b);
			setResult(data);
		} catch (err) {
			setError(extractErrorMessage(err));
			setResult(null);
		} finally {
			setLoading(false);
		}
	}

	return (
		<div className="page">
			<div className="page-intro">
				<span className="page-kicker">비교 분석</span>
				<h1>지역·업종 비교 리포트</h1>
				<p>두 지역(또는 같은 지역의 다른 업종)을 나란히 비교해 AI가 근거를 들어 요약해드립니다.</p>
			</div>

			<QuickLinks />

			<form className="compare-form" onSubmit={handleSubmit}>
				<TargetPicker label="지역 A" pill="pill-a" value={a} onChange={setA} regions={regions} industries={industries} />
				<TargetPicker label="지역 B" pill="pill-b" value={b} onChange={setB} regions={regions} industries={industries} />
				<div className="compare-submit">
					<button className="submit-button" type="submit" disabled={loading || !a.region.trim() || !b.region.trim()}>
						{loading ? "비교 중..." : "비교하기"}
					</button>
				</div>
			</form>

			{error && <div className="error-box">{error}</div>}

			{result && (
				<>
					<div className="result-grid">
						<section className="report-panel">
							<h3 className="compare-label pill-a">지역 A · {result.a.regionName}</h3>
							<StatsSummary stats={result.a} />
						</section>
						<section className="report-panel">
							<h3 className="compare-label pill-b">지역 B · {result.b.regionName}</h3>
							<StatsSummary stats={result.b} />
						</section>
					</div>

					<section className="report-panel">
						<h3 className="compare-label">AI 비교 요약</h3>
						<ReportText text={result.comparison} />
					</section>

					<div className="result-grid">
						<StoreMap stats={result.a} />
						<StoreMap stats={result.b} />
					</div>
				</>
			)}
		</div>
	);
}
