function topBreakdown(breakdown, limit = 5) {
	return Object.entries(breakdown ?? {})
		.sort((a, b) => b[1] - a[1])
		.slice(0, limit);
}

export default function ReportPanel({ result }) {
	const { report, stats } = result;

	return (
		<section className="report-panel">
			<div className="stats-grid">
				<div className="stat-card">
					<span className="stat-label">지역</span>
					<span className="stat-value">{stats.regionName}</span>
				</div>
				<div className="stat-card">
					<span className="stat-label">전체 상가업소 수</span>
					<span className="stat-value">{stats.totalStoreCount.toLocaleString()}개</span>
				</div>
				{stats.industryKeyword && (
					<div className="stat-card">
						<span className="stat-label">{stats.industryKeyword} 경쟁업체 수</span>
						<span className="stat-value">{stats.competitorCount.toLocaleString()}개</span>
					</div>
				)}
			</div>

			<article className="report-text">
				{report.split("\n").map((line, i) =>
					line.trim() ? <p key={i}>{line}</p> : null
				)}
			</article>

			<div className="breakdown">
				<h3>업종별 분포 (상위 5)</h3>
				<ul>
					{topBreakdown(stats.industryBreakdown).map(([name, count]) => (
						<li key={name}>
							<span>{name}</span>
							<span>{count}개</span>
						</li>
					))}
				</ul>
			</div>
		</section>
	);
}
